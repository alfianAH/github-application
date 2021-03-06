package com.dicoding.picodiploma.consumerapp.activities

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.entity.User
import com.dicoding.picodiploma.consumerapp.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerapp.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.consumerapp.helper.MappingHelper
import com.dicoding.picodiploma.consumerapp.viewmodel.DetailActivityViewModel
import com.dicoding.picodiploma.consumerapp.widget.FavoriteUserWidget
import kotlinx.android.synthetic.main.user_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailActivityViewModel: DetailActivityViewModel
    private lateinit var uriWithUsername: Uri

    companion object{
        const val EXTRA_URL_PROFILE = "extra_url_profile"
        const val EXTRA_USER = "extra_user"
        const val REQUEST_UPDATE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        supportActionBar?.elevation = 0f

        showLoading(true)

        detailActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailActivityViewModel::class.java)

        val extraUrlProfile = intent.getStringExtra(EXTRA_URL_PROFILE)

        if (extraUrlProfile != null && !detailActivityViewModel.isUserProfileLoaded) {
            detailActivityViewModel.isUserProfileLoaded = true
            detailActivityViewModel.setUserProfile(extraUrlProfile)
        }

        // Get User Profile
        detailActivityViewModel.getUserProfile().observe(this, { userProfile ->
            if(userProfile != null){
                // content://com.dicoding.picodiploma.githubapplication/favorite_user_tbl/username
                uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + userProfile.username)

                title = userProfile.name // Set the title of activity
                setUser(userProfile)

                // Set tab layout title
                sectionsPagerAdapter.setPageTitle(userProfile)
                // Set follow url
                sectionsPagerAdapter.setFollowUrl(userProfile.followersUrl.toString(),
                    userProfile.followingUrl.toString())

                binding.viewPager.adapter = sectionsPagerAdapter
                binding.tabs.setupWithViewPager(binding.viewPager)

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

    private fun setUser(user: User) {
        val repositoryText = resources.getQuantityString(
            R.plurals.numberOfRepositories, user.repositories as Int, user.repositories)

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
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    /**
     * Set favorite button
     */
    private fun favoriteButtonClickListener(user: User){
        var favoriteStatus = false

        if(isFavoriteUserInDatabase()) {
            // Initial favorite status
            favoriteStatus = true
        }
        setFavoriteStatus(favoriteStatus)

        // When FAB is clicked, change favorite status
        binding.fabFavorite.setOnClickListener{
            favoriteStatus = !favoriteStatus

            val values = ContentValues()

            // If true, add user to table
            if(favoriteStatus){
                values.put(DatabaseContract.FavoriteUserColumns.USERNAME, user.username)
                values.put(DatabaseContract.FavoriteUserColumns.NAME, user.name)
                values.put(DatabaseContract.FavoriteUserColumns.PHOTO_URL, user.photo)
                values.put(DatabaseContract.FavoriteUserColumns.PROFILE_URL, user.urlProfile)
                values.put(DatabaseContract.FavoriteUserColumns.LOCATION, user.location)

                // Use CONTENT_URI to insert
                // content://com.dicoding.picodiploma.githubapplication/favorite_user_tbl/
                contentResolver.insert(CONTENT_URI, values)

                Toast.makeText(this,
                    "${user.username} ${getString(R.string.add_fav_user)}",
                    Toast.LENGTH_SHORT).show()

            } else{ // Else, delete user from table
                // Use uriWithId to delete
                // content://com.dicoding.picodiploma.githubapplication/favorite_user_tbl/username
                contentResolver.delete(uriWithUsername, null, null)

                Toast.makeText(this,
                    "${user.username} ${getString(R.string.remove_fav_user)}",
                    Toast.LENGTH_SHORT).show()
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
    private fun isFavoriteUserInDatabase(): Boolean{
        val cursor = contentResolver.query(uriWithUsername, null, null, null, null)

        if(cursor != null) {
            val user = MappingHelper.mapCursorToArrayList(cursor)

            if (user.size > 0) return true
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