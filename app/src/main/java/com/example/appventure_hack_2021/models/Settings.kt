package com.example.appventure_hack_2021.models

import com.google.android.gms.maps.model.LatLng

data class Settings(
    val home: LatLng? = null,
    val difficulty_idx: Int = 0,
    val theme_idx: Int = 0
)
