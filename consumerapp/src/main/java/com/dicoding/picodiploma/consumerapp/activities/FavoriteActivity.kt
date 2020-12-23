package com.dicoding.picodiploma.consumerapp.activities

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.entity.User
import com.dicoding.picodiploma.consumerapp.adapter.UserAdapter
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerapp.databinding.ActivityFavoriteBinding
import com.dicoding.picodiploma.consumerapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userAdapter: UserAdapter

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

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                loadFavoriteUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

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

    override fun onResume() {
        super.onResume()
        // Load again if activity is resumed
        loadFavoriteUserAsync()
    }

    private fun loadFavoriteUserAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            val deferredFavoriteUser = async(Dispatchers.IO) {
                // CONTENT_URI = content://com.dicoding.picodiploma.githubapplication/fav_user
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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