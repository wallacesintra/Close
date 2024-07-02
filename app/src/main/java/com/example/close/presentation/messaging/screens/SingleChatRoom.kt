package com.example.close.presentation.messaging.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.close.presentation.components.Loading
import com.example.close.presentation.messaging.models.MessageState
import com.example.close.presentation.messaging.viewmodel.MessagingViewModel

@Composable
fun SingleChatRoom(
    chatRoomUid: String,
    currentUserUid: String,
    messagingViewModel: MessagingViewModel
){
    val messageState = messagingViewModel.messageState

    var text: String by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        messagingViewModel.getChatRoomByChatUid(chatRoom = chatRoomUid)
    }

    Column {
        Text(text = "Chat room")
        when(messageState){
            is MessageState.Error -> { Text(text = messageState.error)}
            MessageState.Loading -> { Loading() }
            is MessageState.Success -> {
                val list = messageState.messagesList
//                Text(text = messageState.messagesList.size.toString())
                LazyColumn {
                    items(list){ i ->
                        Text(text = "${i.sender.username}: ${i.message}")
                    }
                }
            }
        }

        TextField(value = text, onValueChange = {text = it})
        Button(onClick = { messagingViewModel.sendMessage(roomUid = chatRoomUid, senderUid = currentUserUid, textMessage = text) }) {
            Text(text = "send text")
        }
    }
}