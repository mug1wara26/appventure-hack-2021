package com.example.appventure_hack_2021

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

fun getDataFromRTDB(databaseRef: DatabaseReference, listener: (DataSnapshot) -> Unit) {
    databaseRef.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            listener(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Database Error", error.details)
        }
    })
}