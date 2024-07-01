package com.example.close.presentation.friends.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.Loading
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.friends.components.FriendRequestContainer
import com.example.close.presentation.friends.models.FriendRequestsState
import com.example.close.presentation.friends.viewmodels.FriendRequestsViewModel

@Composable
fun FriendRequestsScreen(
    currentUserUid: String,
    friendRequestsViewModel: FriendRequestsViewModel,
){

    val requestState = friendRequestsViewModel.friendRequestsState

    LaunchedEffect(key1 = Unit) {
        friendRequestsViewModel.getCurrentUserFriendRequests(currentUserUid)
    }

    when(requestState){
        is FriendRequestsState.Error -> { Text(text = "Error:" + requestState.errorMessage)}
        FriendRequestsState.Loading -> {
            Loading(modifier = Modifier.padding(5.dp))
        }
        is FriendRequestsState.Success -> {
            Column {
                MediumText(text = stringResource(id = R.string.friend_requests) + " ${requestState.requestList.size}" , isBold = true, modifier = Modifier.padding(10.dp))
                LazyColumn {
                    items(requestState.requestList){request ->
                        FriendRequestContainer(closeFriendRequest = request) {
                            friendRequestsViewModel.acceptFriendRequest(closeFriendRequest = request, currentUserUid = currentUserUid)
                        }
                    }
                }

            }
        }
    }
}