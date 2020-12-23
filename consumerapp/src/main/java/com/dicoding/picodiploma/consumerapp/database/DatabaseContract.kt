package com.dicoding.picodiploma.consumerapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.picodiploma.githubapplication"
    const val SCHEME = "content"

    internal class FavoriteUserColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user_tbl"
            const val USERNAME = "username"
            const val NAME = "name"
            const val PHOTO_URL = "photo_url"
            const val PROFILE_URL = "profile_url"
            const val LOCATION = "location"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}