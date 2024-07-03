package com.example.close.presentation.profile.models

import com.example.close.data.database.models.CloseUser

sealed interface DetailsState {
    data class Success(val friendsList: List<CloseUser>): DetailsState

    data object Loading: DetailsState

    data class Error(val errorMessage: String): DetailsState
}