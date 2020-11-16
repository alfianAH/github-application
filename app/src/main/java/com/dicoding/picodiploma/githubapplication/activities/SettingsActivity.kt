package com.dicoding.picodiploma.githubapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.dicoding.picodiploma.githubapplication.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_change.setOnClickListener{
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}