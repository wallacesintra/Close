package com.example.close.presentation.friends.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.close.presentation.friends.components.FriendRequestContainer
import com.example.close.presentation.friends.models.FriendRequestsState
import com.example.close.presentation.friends.viewmodels.FriendRequestsViewModel

@Composable
fun FriendRequestsScreen(
    currentUserUid: String,
    friendRequestsViewModel: FriendRequestsViewModel
){

    val requestState = friendRequestsViewModel.friendRequestsState

    LaunchedEffect(key1 = Unit) {
        friendRequestsViewModel.getCurrentUserFriendRequests(currentUserUid)
    }

    Column {
        Text(text = "Friend Request")

        when(requestState){
            is FriendRequestsState.Error -> { Text(text = "Error:" + requestState.errorMessage)}
            FriendRequestsState.Loading -> { Text(text = "Loading...") }
            is FriendRequestsState.Success -> {
                LazyColumn {
                    items(requestState.requestList){request ->
                        FriendRequestContainer(closeFriendRequest = request) {
                            friendRequestsViewModel.acceptFriendRequest(closeFriendRequest = request, currentUserUid = currentUserUid)
                        }
//                        FriendRequestContainer(senderUid = request.senderUid)
                    }
                }
            }
        }
    }
}