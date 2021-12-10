package com.example.appventure_hack_2021

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseAccessor(val user: FirebaseUser) {
    val ref = Firebase.database.reference
    val userRef = ref.child(user.uid)

    companion object {
        lateinit var instance: DatabaseAccessor
    }
}