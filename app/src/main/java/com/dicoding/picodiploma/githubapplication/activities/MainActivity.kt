package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.User
import com.dicoding.picodiploma.githubapplication.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UserAdapter.OnUserClickListener {
    // Recycler view
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    // Data
    // String
    private lateinit var dataUserName: Array<String>
    private lateinit var dataName: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataCompany: Array<String>
    // Number
    private lateinit var dataRepositories: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    // Photo
    private lateinit var dataPhoto: TypedArray

    private var users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepare() // Prepare data
        addItem() // Add data to adapter

        // Set layout manager and adapter for recycler view
        viewManager = LinearLayoutManager(this)
        viewAdapter = UserAdapter(users, this)

        rv_list.apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    /**
     * Prepare data from resources
     */
    private fun prepare(){
        // Get string
        dataUserName = resources.getStringArray(R.array.username)
        dataName = resources.getStringArray(R.array.name)
        dataLocation = resources.getStringArray(R.array.location)
        dataCompany = resources.getStringArray(R.array.company)

        // Get number
        dataRepositories = resources.getStringArray(R.array.repository)
        dataFollowers = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)

        // Get photo
        dataPhoto = resources.obtainTypedArray(R.array.avatar)
    }

    /**
     * Add item
     */
    private fun addItem(){
        for(position in dataUserName.indices){
            // Set user
            val user = User(
                dataPhoto.getResourceId(position, -1),
                dataUserName[position],
                dataName[position],
                dataLocation[position],
                dataCompany[position],
                dataRepositories[position],
                dataFollowers[position],
                dataFollowing[position]
            )

            users.add(user) // Add user
        }
    }

    /**
     * When user click on list
     */
    override fun onClick(position: Int) {
        val moveIntent = Intent(this, DetailActivity::class.java)
        moveIntent.putExtra(DetailActivity.EXTRA_USER, users[position])
        startActivity(moveIntent)
    }
}