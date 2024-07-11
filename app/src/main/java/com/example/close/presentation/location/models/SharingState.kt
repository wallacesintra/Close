package com.example.close.presentation.location.models

sealed interface SharingState {

    data object Loading: SharingState

    data class Success(val friendsLocationList: List<FriendLocation>) : SharingState

    data class Error(val error: String): SharingState
}