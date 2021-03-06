package com.dicoding.picodiploma.githubapplication.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubapplication.helper.MappingHelper
import java.lang.Exception

internal class StackRemoteViewsFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {

    private val TAG = StackRemoteViewsFactory::class.java.simpleName
    private val widgetItems = ArrayList<String>()

    override fun onCreate() {
        Log.d(TAG, "onCreate")
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()

        val cursor = context.contentResolver.query(
            CONTENT_URI, null, null, null, null)

        val users = MappingHelper.mapCursorToArrayList(cursor)

        if(users.size > 0) {
            widgetItems.clear() // Clear data first
            for (user in users) {
                widgetItems.add(user.photo)
            }
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        try{
            // Get profile picture from link
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(widgetItems[position])
                .submit(460, 460)
                .get()

            rv. setImageViewBitmap(R.id.img_view, bitmap)
        } catch (e: Exception){
            e.printStackTrace()
        }

        val extras = bundleOf(FavoriteUserWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.img_view, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(i: Int): Long = 0
    override fun hasStableIds(): Boolean = false
}