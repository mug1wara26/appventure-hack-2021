package com.example.appventure_hack_2021.models

data class Settings(
    val difficulty_idx: Int = 0,
    val theme_idx: Int = 0,
    val favourites: List<Location> = listOf()
)
