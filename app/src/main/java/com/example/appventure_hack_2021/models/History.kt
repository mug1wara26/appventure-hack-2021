package com.example.appventure_hack_2021.models

import com.google.android.gms.maps.model.LatLng

data class History(
    val start: LatLng,
    val end: LatLng,
    val totalDistance: Float)
