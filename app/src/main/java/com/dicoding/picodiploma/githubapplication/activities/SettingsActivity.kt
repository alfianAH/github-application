package com.dicoding.picodiploma.githubapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)

        supportFragmentManager.beginTransaction().add(R.id.container, SettingsFragment()).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}