package com.dicoding.picodiploma.githubapplication.database

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class FavoriteUserColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user_tbl"
            const val USERNAME = "username"
            const val NAME = "name"
            const val PHOTO_URL = "photo_url"
            const val PROFILE_URL = "profile_url"
            const val LOCATION = "location"
        }
    }
}