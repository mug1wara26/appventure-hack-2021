package com.example.appventure_hack_2021.models

data class History(
    val start: Location,
    val end: Location,
    val startTime: Long,
    val endTime: Long,
    val totalDistance: Float
    )
