package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.User
import com.dicoding.picodiploma.githubapplication.adapter.UserAdapter
import com.dicoding.picodiploma.githubapplication.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Data
    // String
    private lateinit var dataUserName: Array<String>

    private lateinit var userAdapter: UserAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showLoading(true)

        // Get View Model
        mainActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainActivityViewModel::class.java)

        prepareDataFromApi()
        showRecyclerView()

        // Search user
        search_user.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
        mainActivityViewModel.getSearchedUser().observe(this, Observer { users ->
            // If query is null show init data again
            if (users != null) {
                userAdapter.setData(users)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean){
        if(state){
            progress_bar.visibility = View.VISIBLE
        } else{
            progress_bar.visibility = View.GONE
        }
    }

    private fun showRecyclerView(){
        // Set layout manager and adapter for recycler view
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
            override fun onItemClicked(user: User) {
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(moveIntent)
            }
        })
    }

    private fun prepareDataFromApi(){
        // Get username from resources
        if(!mainActivityViewModel.isLoaded) {
            mainActivityViewModel.isLoaded = true
            dataUserName = resources.getStringArray(R.array.username)
            mainActivityViewModel.setInitUsersFromApi(dataUserName)
        }

        mainActivityViewModel.getInitUsersFromApi().observe(this, Observer { users ->
            if (users != null) {
                userAdapter.setData(users)
                showLoading(false)
            }
        })
    }
}