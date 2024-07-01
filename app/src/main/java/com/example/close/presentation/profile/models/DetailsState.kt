package com.example.close.presentation.profile.models

import com.example.close.data.database.models.CloseUsers

sealed interface DetailsState {
    data class Success(val friendsList: List<CloseUsers>): DetailsState

    data object Loading: DetailsState

    data class Error(val errorMessage: String): DetailsState
}