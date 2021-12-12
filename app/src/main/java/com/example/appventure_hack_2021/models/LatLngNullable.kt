package com.example.appventure_hack_2021.models

import com.google.android.gms.maps.model.LatLng

data class LatLngNullable(
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    fun toLatLng() = LatLng(
        latitude!!,
        longitude!!
    )
}