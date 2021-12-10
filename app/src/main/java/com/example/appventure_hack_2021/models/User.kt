package com.example.appventure_hack_2021.models

data class User(
    val uid: String? = null,
    val settings: Settings = Settings(),
    val historyArrayList: ArrayList<History> = ArrayList()
)
