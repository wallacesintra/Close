package com.example.close.presentation.friends.models

import com.example.close.data.database.models.CloseUsers

sealed interface CloseUserState {
    data class Success(val userDetails: CloseUsers): CloseUserState

    data class Error(val error: String): CloseUserState

    data object Loading: CloseUserState
}