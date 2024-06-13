package com.example.close.presentation.auth.models

data class AuthState(
    val isUserSignedIn: Boolean = false,
    val signInError: String? = null
)