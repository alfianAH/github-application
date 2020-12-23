package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.adapter.UserAdapter
import com.dicoding.picodiploma.githubapplication.databinding.ActivityMainBinding
import com.dicoding.picodiploma.githubapplication.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var dataUserName: Array<String>
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        prepareDataFromApi()
        showRecyclerView()

        // Search user
        binding.searchUser.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                mainActivityViewModel.searchUser(query) // Search user
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query?.isEmpty()!!){
                    prepareDataFromApi()
                }
                return false
            }
        })

        // Get searched user
        mainActivityViewModel.getSearchedUser().observe(this, { users ->
            // If query is null show init data again
            if (users != null) {
                userAdapter.setData(users)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> { // Settings
                val moveIntent = Intent(this, SettingsActivity::class.java)
                startActivity(moveIntent)
                return true
            }

            R.id.favorite -> { // Favorite
                val moveIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(moveIntent)
                return true
            }
        }

        return false
    }

    /**
     * Show loading when state is true
     * Hide loading when state is false
     */
    private fun showLoading(state: Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility = View.GONE
        }
    }

    /**
     * Show Recycler View
     */
    private fun showRecyclerView(){
        // Set layout manager and adapter for recycler view
        userAdapter = UserAdapter(this)
        userAdapter.notifyDataSetChanged()

        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = userAdapter
    }

    /**
     * Prepare Initial Data From API
     */
    private fun prepareDataFromApi(){
        // Get View Model
        mainActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainActivityViewModel::class.java)

        // Get username from resources
        if(!mainActivityViewModel.isLoaded) {
            mainActivityViewModel.isLoaded = true
            dataUserName = resources.getStringArray(R.array.username)
            mainActivityViewModel.setInitUsersFromApi(dataUserName)
        }

        mainActivityViewModel.getInitUsersFromApi().observe(this, { users ->
            if (users != null) {
                userAdapter.setData(users)
                showLoading(false)
            }
        })
    }
}