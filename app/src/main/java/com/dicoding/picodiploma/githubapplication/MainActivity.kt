package com.dicoding.picodiploma.githubapplication

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

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

    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set adapter
        adapter = UserAdapter(this)
        lv_list.adapter = adapter

        prepare() // Prepare data
        addItem() // Add data to adapter
    }

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
        adapter.users = users
    }
}