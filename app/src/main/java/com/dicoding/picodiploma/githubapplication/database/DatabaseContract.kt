package com.dicoding.picodiploma.githubapplication.database

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class FavoriteUserColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user_tbl"
            const val _ID = "_id"
            const val USERNAME = "username"
        }
    }
}