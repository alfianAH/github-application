package com.dicoding.picodiploma.githubapplication.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.AUTHORITY
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.githubapplication.database.FavoriteUserHelper

class GithubProvider : ContentProvider() {

    companion object{
        private const val FAV_USER = 1
        private const val FAV_USER_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteUserHelper: FavoriteUserHelper

        init {
            // content://com.dicoding.picodiploma.githubapplication/favorite_user_tbl
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV_USER)

            // content://com.dicoding.picodiploma.githubapplication/favorite_user_tbl/username
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FAV_USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteUserHelper = FavoriteUserHelper.getInstance(context as Context)
        favoriteUserHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            FAV_USER -> favoriteUserHelper.queryAll()
            FAV_USER_ID -> favoriteUserHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when(FAV_USER){
            uriMatcher.match(uri) -> favoriteUserHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, contentValues: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when(FAV_USER_ID){
            uriMatcher.match(uri) -> favoriteUserHelper.update(uri.lastPathSegment.toString(), contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when(FAV_USER_ID){
            uriMatcher.match(uri) -> favoriteUserHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}