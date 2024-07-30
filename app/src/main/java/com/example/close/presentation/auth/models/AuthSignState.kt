package com.example.close.presentation.auth.models

data class AuthState(
    val isUserSignedIn: Boolean = false,
    val signInError: String? = null
)


sealed interface AuthSignState {
    data object Success: AuthSignState

    data class Error(val error: String): AuthSignState

    data object NotSignedIn: AuthSignState

    data object Loading: AuthSignState
}