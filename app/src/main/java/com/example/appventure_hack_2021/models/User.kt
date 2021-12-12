package com.example.appventure_hack_2021.models

data class User(
    val uid: String? = null,
    val settings: Settings = Settings(),
    // val historyList: List<History> = listOf()
    // stored internally as mapping, so cant deserialise here
)
