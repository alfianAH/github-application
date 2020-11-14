package com.dicoding.picodiploma.githubapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubapplication.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainActivityViewModel: ViewModel() {
    private val listInitUsersFromApi = MutableLiveData<ArrayList<User>>()
    private val listSearchedUsers = MutableLiveData<ArrayList<User>>()
    var isLoaded = false

    fun setInitUsersFromApi(username: Array<String>){

        val listItems = ArrayList<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 3bf06b277f7ab67458be2c2b32852c948d9fdc62")
        client.addHeader("User-Agent", "request")

        for(item in username) {
            val url = "https://api.github.com/users/$item"

            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    try {
                        val result = String(responseBody)
                        val responseObject = JSONObject(result)

                        val userPhoto = responseObject.getString("avatar_url")
                        val name = responseObject.getString("name")
                        val urlProfile = responseObject.getString("url")
                        val location = responseObject.getString("location")
                        val company = responseObject.getString("company")
                        val repositories = responseObject.getString("public_repos")
                        val followers = responseObject.getString("followers")
                        val following = responseObject.getString("following")

                        // Set user
                        val user = User(
                            userPhoto, item, name, urlProfile,
                            location, company,
                            repositories, followers, following
                        )

                        listItems.add(user)
                        listInitUsersFromApi.postValue(listItems)
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
    }

    fun searchUser(query: String){
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
                        val urlProfile = user.getString("url")
                        val userPhoto = user.getString("avatar_url")

                        val userItems = User(userPhoto, username, urlProfile = urlProfile)

                        listUsers.add(userItems)
                        listSearchedUsers.postValue(listUsers)
                    }

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

    fun getInitUsersFromApi(): LiveData<ArrayList<User>> = listInitUsersFromApi

    fun getSearchedUser(): LiveData<ArrayList<User>> = listSearchedUsers
}