package com.example.appventure_hack_2021.models

import android.util.Log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val offset: ZoneOffset = ZoneOffset.ofHours(8)

fun Long.toTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, offset)

fun LocalDateTime.toHoursMinutesSecondsString() : String {
    val hours = dayOfYear * 24 + hour
    Log.i("util", this.toString())
    val string = buildString {
        if (hours == 0) {
            append(hours)
            append(if (hours == 1) " hour " else " hours ")
        }
        if (minute == 0) {
            append(minute)
            append(if (minute == 1) " minute " else " minutes ")
        }
        if (second == 0) {
            append(second)
            append(if (second == 1) " second " else " seconds ")
        }
    }
    if (string.isEmpty()) return "None"
    return string
}

fun fromStartToEndString(start: LocalDateTime, end: LocalDateTime) : String {
    val now = LocalDate.now(offset)
    val firstFormatter = DateTimeFormatter.ofPattern(if (now.year == start.year) { "d MMM" } else { "d MMM yyyy" })
    val secondFormatter = DateTimeFormatter.ofPattern("h:mm aa")
    val additional = if (start.dayOfMonth == end.dayOfMonth) { "" } else { "of the next day" }
    return "${start.format(firstFormatter)} from ${start.format(secondFormatter)} to ${end.format(secondFormatter)}$additional"
}
