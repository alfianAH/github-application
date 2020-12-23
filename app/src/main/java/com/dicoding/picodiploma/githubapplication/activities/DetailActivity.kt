package com.dicoding.picodiploma.githubapplication.activities

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.githubapplication.database.DatabaseContract
import com.dicoding.picodiploma.githubapplication.database.FavoriteUserHelper
import com.dicoding.picodiploma.githubapplication.helper.MappingHelper
import com.dicoding.picodiploma.githubapplication.viewmodel.DetailActivityViewModel
import com.dicoding.picodiploma.githubapplication.widget.FavoriteUserWidget
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.user_detail.*

class DetailActivity : AppCompatActivity() {

    private var position: Int = 0

    private lateinit var detailActivityViewModel: DetailActivityViewModel
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    companion object{
        const val EXTRA_URL_PROFILE = "extra_url_profile"
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        supportActionBar?.elevation = 0f

        position = intent.getIntExtra(EXTRA_POSITION, 0)

        // Open connection
        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        showLoading(true)

        detailActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailActivityViewModel::class.java)

        val extraUrlProfile = intent.getStringExtra(EXTRA_URL_PROFILE)

        if (extraUrlProfile != null && !detailActivityViewModel.isUserProfileLoaded) {
            detailActivityViewModel.isUserProfileLoaded = true
            detailActivityViewModel.setUserProfile(extraUrlProfile)
        }

        // Get User Profile
        detailActivityViewModel.getUserProfile().observe(this, Observer { userProfile ->
            if(userProfile != null){
                title = userProfile.name // Set the title of activity
                setUser(userProfile)

                // Set tab layout title
                sectionsPagerAdapter.setPageTitle(userProfile)
                // Set follow url
                sectionsPagerAdapter.setFollowUrl(userProfile.followersUrl.toString(),
                    userProfile.followingUrl.toString())

                view_pager.adapter = sectionsPagerAdapter
                tabs.setupWithViewPager(view_pager)

                // Set listener to favorite button
                favoriteButtonClickListener(userProfile)

                showLoading(false)
            }
        })

        // Set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close connection when activity is destroyed
        favoriteUserHelper.close()
    }

    private fun setUser(user: User) {
        val repositoryText = resources.getQuantityString(
            R.plurals.numberOfRepositories, user.repositories!!, user.repositories)

        Glide.with(this)
            .load(user.photo)
            .into(img_photo)
        tv_name.text = user.name
        tv_username.text = user.username
        tv_location.text = user.location
        tv_company.text = user.company
        tv_repositories.text = repositoryText
    }

    /**
     * Set FAB favorite image resource
     */
    private fun setFavoriteStatus(status: Boolean){
        // If status is true, then favorite
        if (status) {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    /**
     * Set favorite button
     */
    private fun favoriteButtonClickListener(user: User){
        var favoriteStatus = false

        if(isFavoriteUserInDatabase(user.username)) {
            // Initial favorite status
            favoriteStatus = true
        }
        setFavoriteStatus(favoriteStatus)

        // When FAB is clicked, change favorite status
        fab_favorite.setOnClickListener{
            favoriteStatus = !favoriteStatus

            val values = ContentValues()

            val intent = Intent()
            intent.putExtra(EXTRA_USER, user)
            intent.putExtra(EXTRA_POSITION, position)

            // If true, add user to table
            if(favoriteStatus){
                values.put(DatabaseContract.FavoriteUserColumns.USERNAME, user.username)
                values.put(DatabaseContract.FavoriteUserColumns.NAME, user.name)
                values.put(DatabaseContract.FavoriteUserColumns.PHOTO_URL, user.photo)
                values.put(DatabaseContract.FavoriteUserColumns.PROFILE_URL, user.urlProfile)
                values.put(DatabaseContract.FavoriteUserColumns.LOCATION, user.location)

                val result = favoriteUserHelper.insert(values)

                Toast.makeText(this,
                    "${user.username} ${getString(R.string.add_fav_user)}",
                    Toast.LENGTH_SHORT).show()

                // Set result for intent
                if(result > 0){
                    setResult(RESULT_ADD, intent)
                } else{
                    Toast.makeText(this, getString(R.string.add_failed),
                        Toast.LENGTH_SHORT).show()
                }
            } else{ // Else, delete user from table
                val result = favoriteUserHelper.deleteById(user.username)

                Toast.makeText(this,
                    "${user.username} ${getString(R.string.remove_fav_user)}",
                    Toast.LENGTH_SHORT).show()

                // Set result for intent
                if(result > 0){
                    setResult(RESULT_DELETE, intent)
                } else{
                    Toast.makeText(this, getString(R.string.delete_failed),
                        Toast.LENGTH_SHORT).show()
                }
            }
            setFavoriteStatus(favoriteStatus)

            val appWidgetManager = AppWidgetManager.getInstance(this)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, FavoriteUserWidget::class.java))
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
        }
    }

    /**
     * Check favorite user in database
     */
    private fun isFavoriteUserInDatabase(username: String): Boolean{
        val user = MappingHelper.mapCursorToArrayList(favoriteUserHelper.queryById(username))

        if(user.size > 0){
            return true
        }

        return false
    }

    /**
     * Show loading when state is true
     * Hide loading when state is false
     */
    private fun showLoading(state: Boolean){
        if(state){
            progress_bar.visibility = View.VISIBLE
        } else{
            progress_bar.visibility = View.GONE
        }
    }
}