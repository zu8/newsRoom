package com.alzu.android.newsroom.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.presentation.NewsRoomActivity

class PreferencesFragment : PreferenceFragmentCompat() {

    val TAG = "PreferencesFragment"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val sp = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        val themeSwitcher = findPreference<SwitchPreferenceCompat>("theme")
        themeSwitcher?.setOnPreferenceChangeListener { _, newState ->
            val mode = if (newState as Boolean) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
            sp.edit().putBoolean("nightMode",newState).apply()
            activity?.recreate()
            return@setOnPreferenceChangeListener true
        }

        val countryPref = findPreference<ListPreference>("list_preference_countries")
        val country = countryPref?.value
        Log.i(TAG, "$country")
        countryPref?.setOnPreferenceChangeListener { _, newCountry ->
            newCountry?.let {
                val viewModel = (activity as NewsRoomActivity).viewModel
                viewModel.setRegion(it as String)
                with(sp?.edit()) {
                    this?.putString("currentCountry", it)
                    this?.apply()
                }
            }
            return@setOnPreferenceChangeListener true
        }

    }
}