package com.dicoding.picodiploma.consumerapp.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.receiver.AlarmReceiver

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{
    private lateinit var language: String
    private lateinit var reminder: String

    private lateinit var changeLanguagePreference: Preference
    private lateinit var isReminderPreference: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()

        changeLanguagePreference.setOnPreferenceClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            return@setOnPreferenceClickListener true
        }
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
        if(key == reminder){
            if(sharedPreferences.getBoolean(reminder, false)){
                alarmReceiver.setRepeatingAlarm(context as Context, "09:00",
                    getString(R.string.opening_message),
                    getString(R.string.turn_on_reminder))
            } else{
                alarmReceiver.cancelAlarm(context as Context,
                    getString(R.string.turn_off_reminder))
            }
        }
    }

    private fun init(){
        language = resources.getString(R.string.key_language)
        reminder = resources.getString(R.string.key_reminder)

        changeLanguagePreference = findPreference<Preference>(language) as Preference
        isReminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference

        alarmReceiver = AlarmReceiver()
    }
}