package com.example.close.presentation.models

import com.google.firebase.auth.FirebaseUser

data class CloseUserData(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val bio: String = "Close is bringing me closer to you",
    val friends: List<String> = emptyList(),
    var shareLocation: Boolean = false,
)

