package com.dicoding.picodiploma.githubapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.User
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        // Set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = user.name // Set the title of activity

        setUser(user)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setUser(user: User){
        val followersText = "${user.followers} followers"
        val followingText = "${user.following} following"
        val repositoryText = "${user.repositories} repositories"

        img_photo.setImageResource(user.photo)
        tv_name.text = user.name
        tv_username.text = user.username
        follower.text = followersText
        following.text = followingText
        tv_location.text = user.location
        tv_company.text = user.company
        tv_repositories.text = repositoryText
    }
}