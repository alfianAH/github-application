package com.dicoding.picodiploma.githubapplication.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodiploma.githubapplication.User
import com.dicoding.picodiploma.githubapplication.fragments.FollowFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private lateinit var followersValue: String
    private lateinit var followingValue: String

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment = FollowFragment.newInstance(position+1)

//        when(position){
//            0 -> fragment = FollowersFragment.newInstance("Ini followers")
//            1 -> fragment = FollowersFragment.newInstance("Ini following")
//        }

        return fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        val tabTitles: Array<String> = arrayOf(followersValue, followingValue)
        return tabTitles[position]
    }

    fun setPageTitle(user: User){
        followersValue = "${user.followers} followers"
        followingValue = "${user.following} following"
    }
}