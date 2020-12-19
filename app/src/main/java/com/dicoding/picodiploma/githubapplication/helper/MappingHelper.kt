package com.dicoding.picodiploma.githubapplication.helper

import android.database.Cursor
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.PHOTO_URL
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.PROFILE_URL
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.USERNAME

object MappingHelper {

    fun mapCursorToArrayList(favUserCursor: Cursor?): ArrayList<User>{
        val userList = ArrayList<User>()

        favUserCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val photoUrl = getString(getColumnIndexOrThrow(PHOTO_URL))
                val profileUrl = getString(getColumnIndexOrThrow(PROFILE_URL))
                userList.add(User(photoUrl, username, urlProfile = profileUrl))
            }
        }

        return userList
    }
}