package com.example.close.presentation.friends.models

import com.example.close.data.database.models.FriendRequest

sealed interface FriendRequestsState {

    data class Success(val requestList: List<CloseFriendRequest>): FriendRequestsState

    data class Error(val errorMessage: String): FriendRequestsState

    object Loading: FriendRequestsState
}