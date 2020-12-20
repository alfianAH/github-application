package com.dicoding.picodiploma.githubapplication.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.githubapplication.BuildConfig
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract
import com.dicoding.picodiploma.githubapplication.database.FavoriteUserHelper
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.helper.MappingHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class DetailActivityViewModel(application: Application) : AndroidViewModel(application){
    var isFollowProfileLoaded = false
    var isUserProfileLoaded = false

    private lateinit var favoriteUserHelper: FavoriteUserHelper

    private val userProfile = MutableLiveData<User>()
    private val followProfile = MutableLiveData<ArrayList<User>>()
    private val client = AsyncHttpClient()

    private fun addHeaderClient(){
        client.addHeader("Authorization", BuildConfig.ApiKey)
        client.addHeader("User-Agent", "request")
    }

    fun setUserProfile(url: String, favoriteBtn: FloatingActionButton){
        addHeaderClient()

        // Open connection
        favoriteUserHelper = FavoriteUserHelper.getInstance(getApplication())
        favoriteUserHelper.open()

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

                    // Set listener to favorite button
                    favoriteButtonClickListener(user, favoriteBtn)

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

    /**
     * Set FAB favorite image resource
     */
    private fun setFavoriteStatus(status: Boolean, favoriteBtn: FloatingActionButton){
        // If status is true, then favorite
        if (status) {
            favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    /**
     * Set favorite button
     */
    private fun favoriteButtonClickListener(user: User, favoriteBtn: FloatingActionButton){
        var favoriteStatus = false

        if(isFavoriteUserInDatabase(user.username)) {
            // Initial favorite status
            favoriteStatus = true
        }
        setFavoriteStatus(favoriteStatus, favoriteBtn)

        // When FAB is clicked, change favorite status
        favoriteBtn.setOnClickListener{
            favoriteStatus = !favoriteStatus

            val values = ContentValues()

            // If true, add user to table
            if(favoriteStatus){
                values.put(DatabaseContract.FavoriteUserColumns.USERNAME, user.username)
                values.put(DatabaseContract.FavoriteUserColumns.NAME, user.name)
                values.put(DatabaseContract.FavoriteUserColumns.PHOTO_URL, user.photo)
                values.put(DatabaseContract.FavoriteUserColumns.PROFILE_URL, user.urlProfile)
                values.put(DatabaseContract.FavoriteUserColumns.LOCATION, user.location)

                favoriteUserHelper.insert(values)
            } else{ // Else, delete user from table
                favoriteUserHelper.deleteById(user.username)
            }
            setFavoriteStatus(favoriteStatus, favoriteBtn)
        }
    }

    /**
     * Check favorite user in database
     */
    private fun isFavoriteUserInDatabase(username: String): Boolean{
        val user = MappingHelper.mapCursorToArrayList(favoriteUserHelper.queryById(username))

        if(user.size > 0){
            return true
        }

        return false
    }
}