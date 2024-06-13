package com.example.close.presentation.models

import com.google.firebase.auth.FirebaseUser

data class UserData(
    val data : FirebaseUser? = null,
    val errorMessage: String? = null,
    val friends: List<FirebaseUser>? = null
)

