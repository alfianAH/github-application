package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

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
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
//                searchUser(query)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun searchUser(query: String){
        val listUsers = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=$query"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 3bf06b277f7ab67458be2c2b32852c948d9fdc62")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)

                        val username = user.getString("login")
                        val userPhoto = user.getString("avatar_url")
                        val urlProfile = user.getString("url")

                        Log.d("Tes", "$username, $userPhoto, $urlProfile")
//                        val userItems = User()
//                        listUsers.add()
                    }

                    showLoading(false)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
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
            mainActivityViewModel.setUsersFromApi(dataUserName)
        }

        mainActivityViewModel.getUsersFromApi().observe(this, Observer { users ->
            if (users != null) {
                userAdapter.setData(users)
                showLoading(false)
            }
        })
    }
}