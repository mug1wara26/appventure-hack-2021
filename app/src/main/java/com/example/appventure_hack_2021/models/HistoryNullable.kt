package com.example.appventure_hack_2021.models

data class HistoryNullable(
    val start: LocationDataNullable = LocationDataNullable(),
    val end: LocationDataNullable = LocationDataNullable(),
    val startTime: Long? = null,
    val endTime: Long? = null,
    val totalDistance: Int? = null
) {
    fun toHistory() = History(
        start.toLocationData(),
        end.toLocationData(),
        startTime!!,
        endTime!!,
        totalDistance!!
    )
}
