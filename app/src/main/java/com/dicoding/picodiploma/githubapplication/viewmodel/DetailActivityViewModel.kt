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

class DetailActivityViewModel : ViewModel(){
    private val userProfile = MutableLiveData<User>()

    fun setUserProfile(url: String){
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 3bf06b277f7ab67458be2c2b32852c948d9fdc62")
        client.addHeader("User-Agent", "request")

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
                    val repositories = responseObject.getString("public_repos")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")

                    // Set user
                    val user = User(
                        userPhoto, username, name, url,
                        location, company,
                        repositories, followers, following
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

            }

        })
    }

    fun getUserProfile(): LiveData<User> = userProfile

    private fun checkNullData(data: String?): String{
        return if(data == "null"){
            "-"
        } else{
            data.toString()
        }
    }
}