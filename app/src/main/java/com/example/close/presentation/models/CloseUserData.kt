package com.example.close.presentation.models

import com.google.firebase.auth.FirebaseUser

data class CloseUserData(
    val uid: String = "",
    val username: String = "",
    val email: String? = "",
    val friends: List<FirebaseUser> = emptyList(),
    val shareLocation: Boolean = false,
)

