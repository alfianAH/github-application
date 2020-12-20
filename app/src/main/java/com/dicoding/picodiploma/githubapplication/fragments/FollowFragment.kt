package com.dicoding.picodiploma.githubapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.entity.User
import com.dicoding.picodiploma.githubapplication.activities.DetailActivity
import com.dicoding.picodiploma.githubapplication.adapter.UserAdapter
import com.dicoding.picodiploma.githubapplication.viewmodel.DetailActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_follow.rv_list

class FollowFragment : Fragment() {

    private lateinit var urlFollow: String
    private lateinit var userAdapter: UserAdapter
    private lateinit var detailActivityViewModel: DetailActivityViewModel

    companion object{
        private const val ARG_URL_PROFILE = "url_follow"

        fun newInstance(urlFollow: String): FollowFragment{
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putString(ARG_URL_PROFILE, urlFollow)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null){
            urlFollow = arguments?.getString(ARG_URL_PROFILE).toString()
            showLoading(true)
            showRecyclerView()
            prepareFollowDataFromApi()
        }
    }

    /**
     * Show Recycler View
     */
    private fun showRecyclerView(){
        // Set layout manager and adapter for recycler view
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        rv_list.layoutManager = LinearLayoutManager(activity)
        rv_list.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
            override fun onItemClicked(user: User) {
                val moveIntent = Intent(activity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_URL_PROFILE, user.urlProfile)
                startActivity(moveIntent)
            }
        })
    }

    private fun prepareFollowDataFromApi(){
        // Get View Model
        detailActivityViewModel = ViewModelProvider(this)
            .get(DetailActivityViewModel::class.java)

        // Set Data
        if(!detailActivityViewModel.isFollowProfileLoaded){
            detailActivityViewModel.isFollowProfileLoaded = true
            detailActivityViewModel.setFollowProfile(urlFollow)
        }

        // Get Data
        detailActivityViewModel.getFollowProfile().observe(viewLifecycleOwner, Observer { users ->
            // If query is null show init data again
            if (users != null) {
                userAdapter.setData(users)
                showLoading(false)
            }
        })
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