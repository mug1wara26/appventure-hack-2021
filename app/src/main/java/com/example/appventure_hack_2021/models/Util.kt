package com.example.appventure_hack_2021.models

import android.widget.TextView
import com.example.appventure_hack_2021.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val offset: ZoneOffset = ZoneOffset.ofHours(8)

fun Long.toTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, offset)

fun LocalDateTime.toHoursMinutesSecondsString() : String {
    val hours = this.dayOfYear * 24 + this.hour
    return (if (hours == 0) "" else "$hours hours ") +
            (if (this.minute == 0) "" else "${this.minute} minutes ") +
            "${this.second} this"
}

fun fromStartToEndString(start: LocalDateTime, end: LocalDateTime) : String {
    val now = LocalDate.now(offset)
    val firstFormatter = DateTimeFormatter.ofPattern(if (now.year == start.year) { "d MMM" } else { "d MMM yyyy" })
    val secondFormatter = DateTimeFormatter.ofPattern("h:mm aa")
    val additional = if (start.dayOfMonth == end.dayOfMonth) { "" } else { "of the next day" }
    return "${start.format(firstFormatter)} from ${start.format(secondFormatter)} to ${end.format(secondFormatter)}$additional"
}
