package com.dicoding.picodiploma.githubapplication.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.picodiploma.githubapplication.R

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{
    private lateinit var LANGUAGE: String
    private lateinit var REMINDER: String

    private lateinit var changeLanguagePreference: Preference
    private lateinit var isReminderPreference: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if(key == REMINDER){
            isReminderPreference.isChecked = sharedPreferences.getBoolean(REMINDER, false)
        }
    }

    private fun init(){
        LANGUAGE = resources.getString(R.string.key_language)
        REMINDER = resources.getString(R.string.key_reminder)

        changeLanguagePreference = findPreference<Preference>(LANGUAGE) as Preference
        isReminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
    }

    private fun setSummaries(){
        val sh = preferenceManager.sharedPreferences

        isReminderPreference.isChecked = sh.getBoolean(REMINDER, false)
        changeLanguagePreference.setOnPreferenceClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            return@setOnPreferenceClickListener true
        }
    }
}