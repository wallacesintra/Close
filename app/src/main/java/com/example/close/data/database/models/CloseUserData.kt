package com.example.close.data.database.models

data class CloseUserData(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val bio: String = "Close is bringing me closer to you",
    val profileImg: String = "female_dp",
    val friends: List<String> = emptyList(),
    var shareLocation: Boolean = false,
)
