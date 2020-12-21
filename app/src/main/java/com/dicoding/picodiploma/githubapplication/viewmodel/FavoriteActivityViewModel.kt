package com.dicoding.picodiploma.githubapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubapplication.database.FavoriteUserHelper
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivityViewModel: ViewModel() {

    var isFavoriteUserLoaded: Boolean = false

    private val deferredUser = MutableLiveData<ArrayList<User>>()

    /**
     * Load favorite user asynchronously
     */
    fun loadFavoriteUserAsync(favoriteUserHelper: FavoriteUserHelper){
        val listDeferredUsers = ArrayList<User>()
        Log.d("async", "LoadAsync")
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavoriteUser = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            listDeferredUsers.addAll(deferredFavoriteUser.await())
            deferredUser.postValue(listDeferredUsers)
        }
    }

    fun getFavoriteUser(): LiveData<ArrayList<User>> = deferredUser
}