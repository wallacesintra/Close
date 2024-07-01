package com.example.close.presentation.friends.models

sealed interface FriendRequestsState {

    data class Success(val requestList: List<CloseFriendRequest>): FriendRequestsState

    data class Error(val errorMessage: String): FriendRequestsState

    data object Loading: FriendRequestsState
}