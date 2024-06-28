package com.example.close.data.database.models

data class CloseUsers(
    val uid: String = "",
    val username: String = "",
    val bio: String = "",
    var sharingLocation: Boolean = false
)