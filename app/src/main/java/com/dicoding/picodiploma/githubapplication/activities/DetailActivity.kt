package com.dicoding.picodiploma.githubapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.User
import com.dicoding.picodiploma.githubapplication.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.githubapplication.viewmodel.DetailActivityViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.user_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailActivityViewModel: DetailActivityViewModel

    companion object{
        const val EXTRA_URL_PROFILE = "extra_url_profile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

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
                showLoading(false)
            }
        })

        // Set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initial favorite status
        var favoriteStatus = false
        setFavoriteStatus(favoriteStatus)

        // When FAB is clicked, change favorite status
        fab_favorite.setOnClickListener{
            favoriteStatus = !favoriteStatus
            setFavoriteStatus(favoriteStatus)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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
        if(status){
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else{
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
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