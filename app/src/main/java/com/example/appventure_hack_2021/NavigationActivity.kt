package com.example.appventure_hack_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appventure_hack_2021.databinding.NavigationActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.appventure_hack_2021.fragments.HomeFragment
import com.example.appventure_hack_2021.fragments.MapFragment
import com.example.appventure_hack_2021.fragments.SettingsFragment





class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: NavigationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = NavigationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item -> // By using switch we can easily get
            // the selected fragment
            // by using there id.
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment = HomeFragment()
                R.id.nav_map -> selectedFragment = MapFragment()
                R.id.nav_settings -> selectedFragment = SettingsFragment()
            }
            // It will help to replace the
            // one fragment to other.
            if (selectedFragment != null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }
            true
        }

        val navView: BottomNavigationView = binding.navView
        navView.setOnNavigationItemSelectedListener(navListener)

        navView.selectedItemId = R.id.nav_home
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
    }


}