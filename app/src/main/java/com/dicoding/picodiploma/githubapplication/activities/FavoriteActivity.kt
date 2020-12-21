package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.adapter.UserAdapter
import com.dicoding.picodiploma.githubapplication.database.FavoriteUserHelper
import com.dicoding.picodiploma.githubapplication.helper.MappingHelper
import com.dicoding.picodiploma.githubapplication.viewmodel.FavoriteActivityViewModel
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteActivityViewModel: FavoriteActivityViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FavoriteActivityViewModel::class.java)

        // Set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_user)

        showRecyclerView()

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

        favoriteActivityViewModel.getFavoriteUser().observe(this, Observer { favoriteUser ->
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

    /**
     * Load favorite user asynchronously
     */
    private fun loadFavoriteUserAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavoriteUser = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favoriteUsers = deferredFavoriteUser.await()

            if(favoriteUsers.size > 0){
                userAdapter.setData(favoriteUsers)
            } else{
                userAdapter.setData(ArrayList())
            }
        }
    }

    /**
     * Show Recycler View
     */
    private fun showRecyclerView(){
        userAdapter = UserAdapter(this)
        userAdapter.notifyDataSetChanged()

        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = userAdapter
    }
}