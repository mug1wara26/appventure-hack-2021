package com.example.appventure_hack_2021

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.appventure_hack_2021.databinding.ActivityMainBinding
import com.example.appventure_hack_2021.models.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private const val RC_SIGN_IN = 1
val database = FirebaseDatabase.getInstance("https://appventure-hackathon-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
lateinit var userRef: DatabaseReference
lateinit var user: User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            RC_SIGN_IN -> {
                val response = IdpResponse.fromResultIntent(data)

                if (resultCode == Activity.RESULT_OK) {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser

                    if(firebaseUser != null) {
                        userRef = database.child("users/${firebaseUser.uid}")
                        database.child("users/${firebaseUser.uid}").get().addOnSuccessListener {
                            // Check if user exists
                            if (!it.exists()) {
                                user = User(firebaseUser.uid)
                                userRef.setValue(user)
                            }
                            else user = it.getValue(User::class.java)!!

                            Log.i("user", user.toString())
                        }

                    startNavigation()
                    }
                } else {
                    println(response?.error)
                }
            }
        }
    }

    private fun startNavigation() {
        val intent = Intent(applicationContext, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }
}