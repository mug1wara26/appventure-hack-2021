package com.example.appventure_hack_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appventure_hack_2021.databinding.ActivityNavigationBinding
import com.example.appventure_hack_2021.fragments.HistoryFragment
import com.example.appventure_hack_2021.fragments.HomeFragment
import com.example.appventure_hack_2021.fragments.MapFragment
import com.example.appventure_hack_2021.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class NavigationActivity : AppCompatActivity() {
    interface OnEnterListener {
        fun onEnter()
    }
    private lateinit var binding: ActivityNavigationBinding
    val fragments = mapOf(
        R.id.nav_map to MapFragment(),
        R.id.nav_home to HomeFragment(),
        R.id.nav_settings to SettingsFragment(),
        R.id.nav_history to HistoryFragment()
    )
    lateinit var navView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navView.setOnNavigationItemSelectedListener {
            replaceFragment(it.itemId)
            true
        }
        navView.selectedItemId = R.id.nav_home
        replaceFragment(R.id.nav_home)
    }

    fun replaceFragment(fragment_id: Int) {
        val fragment = fragments[fragment_id] ?: throw NullPointerException()
        try {
            (fragment as OnEnterListener).onEnter()
        } catch (e: ClassCastException) {}

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}