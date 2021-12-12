package com.example.appventure_hack_2021

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.appventure_hack_2021.databinding.ActivityMainBinding
import com.example.appventure_hack_2021.fragments.modes
import com.example.appventure_hack_2021.models.User
import com.example.appventure_hack_2021.models.getData
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

val database = FirebaseDatabase.getInstance("https://appventure-hackathon-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
lateinit var firebaseUser: FirebaseUser
lateinit var userRef: DatabaseReference
lateinit var user: User

fun refreshUser() {
    userRef.get().addOnSuccessListener {
        user = it.getValue(User::class.java)!!
    }
}
var login_complete = false
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "onCreate Called, $login_complete")
        if (login_complete) return
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Choose authentication providers
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build()
        )

        // Create and launch sign-in intent
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val response = IdpResponse.fromResultIntent(result.data)

            if (result.resultCode == Activity.RESULT_OK) {

                val firebaseUserOrNull = FirebaseAuth.getInstance().currentUser

                if (firebaseUserOrNull != null) {
                    firebaseUser = firebaseUserOrNull
                    userRef = database.child("users/${firebaseUser.uid}")
                    login_complete = true
                } else {
                    Log.e("MainActivity Login", "Despite activity success, firebaseUser is null")
                }
            } else {
                println(response?.error)
            }
        }.launch(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .build())

        Log.i("MainActivity", "onCreate end")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume called $login_complete")
        if (login_complete) {
            userRef.getData {
                // Check if user exists
                if (!it.exists()) {
                    user = User(firebaseUser.uid)
                    userRef.setValue(user)
                } else user = it.getValue(User::class.java)!!

                Log.i("user", user.toString())

                startNavigation()
            }
        }
    }

    private fun startNavigation() {
        Log.i("MainActivity", "starting NavigationActivity")
        AppCompatDelegate.setDefaultNightMode(modes[user.settings.theme_idx])
        val intent = Intent(applicationContext, NavigationActivity::class.java)
        startActivity(intent)
        // shouldStartNavigation = false
        finish()
    }
}