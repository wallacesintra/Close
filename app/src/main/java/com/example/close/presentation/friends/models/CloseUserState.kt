package com.example.close.presentation.friends.models

import com.example.close.data.users.models.CloseUser

sealed interface CloseUserState {
    data class Success(val userDetails: CloseUser): CloseUserState

    data class Error(val error: String): CloseUserState

    data object Loading: CloseUserState
}