package com.example.appventure_hack_2021.models

data class LocationDataNullable(
    val name: String? = null,
    val pos: LatLngNullable = LatLngNullable()
) {
    fun toLocationData() = LocationData(
        name!!,
        pos.toLatLng()
    )
}