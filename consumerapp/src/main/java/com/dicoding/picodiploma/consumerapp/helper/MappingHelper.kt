package com.dicoding.picodiploma.consumerapp.helper

import android.database.Cursor
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.LOCATION
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.NAME
import com.dicoding.picodiploma.consumerapp.entity.User
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.PHOTO_URL
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.PROFILE_URL
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.USERNAME

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User>{
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val photoUrl = getString(getColumnIndexOrThrow(PHOTO_URL))
                val profileUrl = getString(getColumnIndexOrThrow(PROFILE_URL))
                val location = getString(getColumnIndexOrThrow(LOCATION))

                userList.add(User(photoUrl, username, name, profileUrl, location))
            }
        }

        return userList
    }
}