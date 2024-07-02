package com.example.close.presentation.messaging.screens

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
import com.example.close.presentation.components.LargeText
import com.example.close.presentation.components.Loading
import com.example.close.presentation.messaging.components.ChatRoomContainer
import com.example.close.presentation.messaging.models.ChatRoomsState
import com.example.close.presentation.messaging.viewmodel.MessagingViewModel

@Composable
fun MessageScreen(
    currentUserUid: String,
    messagingViewModel: MessagingViewModel
){
    val chatRoomsState = messagingViewModel.chatRoomsState

    LaunchedEffect(key1 = Unit) {
        messagingViewModel.getCurrentUserChatRooms(currentUserUid = currentUserUid)
    }

    when(chatRoomsState){
        is ChatRoomsState.Error -> { Text(text = "Error")}
        ChatRoomsState.Loading -> { Loading() }
        is ChatRoomsState.Success -> {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                LargeText(text = stringResource(id = R.string.chats), modifier = Modifier.padding(vertical = 10.dp))

                LazyColumn {
                    items(chatRoomsState.chatRoomList) {room ->
                        ChatRoomContainer(
                            closeChatRoomUi = room,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                }
            }

        }
    }
}