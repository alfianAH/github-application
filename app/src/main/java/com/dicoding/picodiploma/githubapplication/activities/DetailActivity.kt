package com.dicoding.picodiploma.githubapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.User
import com.dicoding.picodiploma.githubapplication.viewmodel.DetailActivityViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailActivityViewModel: DetailActivityViewModel

    companion object{
        const val EXTRA_URL_PROFILE = "extra_url_profile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailActivityViewModel::class.java)

        val extraUrlProfile = intent.getStringExtra(EXTRA_URL_PROFILE)

        if (extraUrlProfile != null) {
            detailActivityViewModel.setUserProfile(extraUrlProfile)
        }

        // Get User Profile
        detailActivityViewModel.getUserProfile().observe(this, Observer { userProfile ->
            if(userProfile != null){
                title = userProfile.name // Set the title of activity
                setUser(userProfile)
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
        val followersText = "${user.followers} followers"
        val followingText = "${user.following} following"
        val repositoryText = "${user.repositories} repositories"

        Glide.with(this)
            .load(user.photo)
            .into(img_photo)
        tv_name.text = user.name
        tv_username.text = user.username
        follower.text = followersText
        following.text = followingText
        tv_location.text = user.location
        tv_company.text = user.company
        tv_repositories.text = repositoryText
    }
}