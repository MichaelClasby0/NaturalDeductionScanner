package com.hack.naturaldeductionscanner.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.hack.naturaldeductionscanner.R
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingsFragment()
            )
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //Clicking back button to go back to main activity
        btnBack.setOnClickListener {
            finish()
        }
    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}