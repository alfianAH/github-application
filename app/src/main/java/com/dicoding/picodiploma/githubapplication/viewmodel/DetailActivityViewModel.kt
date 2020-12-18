package com.dicoding.picodiploma.githubapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubapplication.BuildConfig
import com.dicoding.picodiploma.githubapplication.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class DetailActivityViewModel : ViewModel(){
    var isFollowProfileLoaded = false
    var isUserProfileLoaded = false

    private val userProfile = MutableLiveData<User>()
    private val followProfile = MutableLiveData<ArrayList<User>>()
    private val client = AsyncHttpClient()

    private fun addHeaderClient(){
        client.addHeader("Authorization", BuildConfig.ApiKey)
        client.addHeader("User-Agent", "request")
    }

    fun setUserProfile(url: String){
        addHeaderClient()
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    val userPhoto = responseObject.getString("avatar_url")
                    val username = responseObject.getString("login")
                    val name = responseObject.getString("name")
                    val location = checkNullData(responseObject.getString("location"))
                    val company = checkNullData(responseObject.getString("company"))
                    val repositories = responseObject.getInt("public_repos")
                    val followers = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")
                    val followersUrl = responseObject.getString("followers_url")
                    val followingUrl = "$url/following"

                    // Set user
                    val user = User(
                        userPhoto, username, name, url,
                        location, company,
                        repositories, followers, following, followersUrl, followingUrl
                    )

                    userProfile.postValue(user)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("Exception", error?.message.toString())
            }
        })
    }

    fun setFollowProfile(followUrl: String){
        val listUsers = ArrayList<User>()

        addHeaderClient()

        client.get(followUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val list = JSONArray(result)

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)

                        val username = user.getString("login")
                        val urlProfile = user.getString("url")
                        val userPhoto = user.getString("avatar_url")

                        val userItems = User(userPhoto, username, urlProfile = urlProfile)

                        listUsers.add(userItems)
                        followProfile.postValue(listUsers)
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

    fun getUserProfile(): LiveData<User> = userProfile

    fun getFollowProfile(): LiveData<ArrayList<User>> = followProfile

    private fun checkNullData(data: String?): String{
        return if(data == "null"){
            "-"
        } else{
            data.toString()
        }
    }
}