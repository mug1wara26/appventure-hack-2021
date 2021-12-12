package com.example.appventure_hack_2021

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

open class GetDataFromRTDB(
    private val databaseRef: DatabaseReference,
    private val listener: GetDataListener
) {
    interface GetDataListener {
        fun onSuccess(snapshot: DataSnapshot)
    }

    fun getData(){
        databaseRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listener.onSuccess(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
               Log.e("Database Error", error.details)
            }

        })
    }
}