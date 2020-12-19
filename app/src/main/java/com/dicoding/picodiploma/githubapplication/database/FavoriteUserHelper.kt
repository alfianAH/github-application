package com.dicoding.picodiploma.githubapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion._ID
import java.sql.SQLException

class FavoriteUserHelper(context: Context) {

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteUserHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): FavoriteUserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: FavoriteUserHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    /**
     * Open connection to database
     */
    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    /**
     * Close connection from database
     */
    fun close(){
        databaseHelper.close()

        if(database.isOpen) database.close()
    }

    /**
     * Get all data in table
     */
    fun queryAll(): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    /**
     * Get data by id
     */
    fun queryById(id: String): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    /**
     * Insert data to table
     */
    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    /**
     * Update data in table
     */
    fun update(id: String, values: ContentValues?): Int{
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    /**
     * Delete certain data in table
     */
    fun deleteById(id: String): Int{
        return database.delete(DATABASE_TABLE, "$_ID = $id", null)
    }
}