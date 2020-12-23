package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
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
import com.dicoding.picodiploma.githubapplication.viewmodel.FavoriteActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteActivityViewModel: FavoriteActivityViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FavoriteActivityViewModel::class.java)

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

        loadFavoriteUser()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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
        favoriteActivityViewModel.isFavoriteUserLoaded = false
        loadFavoriteUser()
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

    private fun loadFavoriteUser(){
        if(!favoriteActivityViewModel.isFavoriteUserLoaded){
            favoriteActivityViewModel.isFavoriteUserLoaded = true
            favoriteActivityViewModel.loadFavoriteUserAsync(favoriteUserHelper)
        }

        favoriteActivityViewModel.getFavoriteUser().observe(this, { favoriteUser ->
            if(favoriteUser != null){

                if(favoriteUser.size > 0){
                    userAdapter.setData(favoriteUser)
                } else{
                    Toast.makeText(this, getString(R.string.empty_fav_user), Toast.LENGTH_SHORT).show()
                    userAdapter.setData(ArrayList())
                }

            }
        })
    }

    private fun loadFavoriteUserAsync(){
        val listDeferredUsers = ArrayList<User>()
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavoriteUser = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            listDeferredUsers.addAll(deferredFavoriteUser.await())

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