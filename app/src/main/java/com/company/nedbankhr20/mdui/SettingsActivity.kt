package com.company.nedbankhr20.mdui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.company.nedbankhr20.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val settingsFragment = SettingsFragment()
        settingsFragment.retainInstance = true
        supportFragmentManager.beginTransaction().replace(R.id.settings_container, settingsFragment).commit()
    }
}