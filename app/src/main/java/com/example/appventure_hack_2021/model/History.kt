package com.example.appventure_hack_2021.model

import com.google.android.gms.maps.model.LatLng

data class History(val start: LatLng? = null, val end: LatLng? = null, val totalDistance: Float? = null)
