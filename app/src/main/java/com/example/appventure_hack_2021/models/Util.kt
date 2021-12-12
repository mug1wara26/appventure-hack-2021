package com.example.appventure_hack_2021.models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun getListener(callback: (DataSnapshot) -> Unit): ValueEventListener {
    return object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            callback(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Database Error", error.details)
        }
    }
}

fun DatabaseReference.getData(callback: (DataSnapshot) -> Unit) {
    this.addListenerForSingleValueEvent(getListener(callback))
}

val offset: ZoneOffset = ZoneOffset.ofHours(8)

fun Long.toTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this / 1000, 0, offset)

fun Duration.toFormattedString() : String {
    if (isZero) return "None"
    val string = buildString {
        val hours = toHours()
        if (hours != 0L) {
            append(hours)
            append(if (hours == 1L) " hour " else " hours ")
        }
        val minutes = seconds / 60 // toMinutesPart()
        if (minutes != 0L) {
            append(minutes)
            append(if (minutes == 1L) " minute " else " minutes ")
        }
        val seconds = seconds % 60 // toSecondsPart()
        if (seconds != 0L) {
            append(seconds)
            append(if (seconds == 1L) " second " else " seconds ")
        }
    }
    return string
}

fun fromStartToEndString(start: LocalDateTime, end: LocalDateTime) : String {
    val now = LocalDate.now(offset)
    val firstFormatter = DateTimeFormatter.ofPattern(if (now.year == start.year) { "d MMM" } else { "d MMM yyyy" })
    val secondFormatter = DateTimeFormatter.ofPattern("h:mm a")
    val additional = if (start.dayOfMonth == end.dayOfMonth) { "" } else { "of the next day" }
    return "${start.format(firstFormatter)} from ${start.format(secondFormatter)} to ${end.format(secondFormatter)}$additional"
}
