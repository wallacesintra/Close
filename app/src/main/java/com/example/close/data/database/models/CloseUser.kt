package com.example.close.data.database.models

data class CloseUser(
    val uid: String = "",
    val username: String = "",
    val bio: String = "",
    val profileImg: String = "female_dp",
    var sharingLocation: Boolean = false
)