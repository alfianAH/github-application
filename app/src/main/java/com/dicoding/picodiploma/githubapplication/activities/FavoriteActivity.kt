package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.adapter.UserAdapter
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubapplication.database.FavoriteUserHelper
import com.dicoding.picodiploma.githubapplication.databinding.ActivityFavoriteBinding
import com.dicoding.picodiploma.githubapplication.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    companion object{
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_user)

        showRecyclerView()

//        val handlerThread = HandlerThread("DataObserver")
//        handlerThread.start()
//        val handler = Handler(handlerThread.looper)
//
//        val myObserver = object : ContentObserver(handler){
//            override fun onChange(selfChange: Boolean) {
//                super.onChange(selfChange)
//                loadFavoriteUser()
//            }
//        }
//
//        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // Open connection
        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        if(savedInstanceState == null){
            loadFavoriteUserAsync()
        } else{
            val users = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if(users != null){
                userAdapter.users = users
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, userAdapter.users)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close connection when activity is destroyed
        favoriteUserHelper.close()
    }

    override fun onResume() {
        super.onResume()
        // Open and load again if activity is resumed
        favoriteUserHelper.open()
        loadFavoriteUserAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){
            when(requestCode){
                DetailActivity.REQUEST_UPDATE ->
                    when(resultCode){
                        DetailActivity.RESULT_ADD -> {
                            val user = data.getParcelableExtra<User>(DetailActivity.EXTRA_USER) as User

                            userAdapter.addItem(user)
                        }

                        DetailActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0)

                            userAdapter.removeItem(position)
                        }
                    }

            }
        }
    }

    private fun loadFavoriteUserAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            val deferredFavoriteUser = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            binding.progressBar.visibility = View.INVISIBLE
            val favoriteUsers = deferredFavoriteUser.await()

            // Set data in userAdapter
            if(favoriteUsers.size > 0){
                userAdapter.users = favoriteUsers
            } else{
                userAdapter.users = ArrayList()
                Toast.makeText(applicationContext,
                    getString(R.string.empty_fav_user), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Show Recycler View
     */
    private fun showRecyclerView(){
        userAdapter = UserAdapter(this)
        userAdapter.notifyDataSetChanged()

        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = userAdapter
    }
}