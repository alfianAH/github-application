package com.dicoding.picodiploma.githubapplication.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.fragments.FollowFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private lateinit var followersValue: String
    private lateinit var followingValue: String
    private lateinit var followersUrl: String
    private lateinit var followingUrl: String

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = FollowFragment.newInstance(followersUrl)

        when(position){
            0 -> fragment = FollowFragment.newInstance(followersUrl)
            1 -> fragment = FollowFragment.newInstance(followingUrl)
        }

        return fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        val tabTitles: Array<String> = arrayOf(followersValue, followingValue)
        return tabTitles[position]
    }

    fun setPageTitle(user: User){
        followersValue = context.resources.getQuantityString(
            R.plurals.numberOfFollowers, user.followers as Int, user.followers)
        followingValue = "${user.following} ${context.getString(R.string.following)}"
    }

    fun setFollowUrl(followersUrl: String, followingUrl: String){
        this.followersUrl = followersUrl
        this.followingUrl = followingUrl
    }
}