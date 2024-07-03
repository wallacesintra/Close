package com.example.close.presentation.messaging.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.Loading
import com.example.close.presentation.messaging.components.ChatBubble
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

    Box(modifier = Modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier
                //            .weight(1.0f)
                .padding(10.dp)
        ) {
            Text(text = "Chat room")
            when (messageState) {
                is MessageState.Error -> {
                    Text(text = messageState.error)
                }

                MessageState.Loading -> {
                    Loading()
                }

                is MessageState.Success -> {
                    val list = messageState.messagesList
                    //                Text(text = messageState.messagesList.size.toString())
                    LazyColumn(
                        modifier = Modifier
                            .weight(1.0f)
                            .padding(bottom = 70.dp)
                    ) {
                        items(list) { message ->
                            //                        Text(text = "${i.sender.username}: ${i.message}")
                            ChatBubble(currentUserUid = currentUserUid, messageUI = message)
                        }
                    }
                }
            }


            //        Button(onClick = { messagingViewModel.sendMessage(roomUid = chatRoomUid, senderUid = currentUserUid, textMessage = text) }) {
            //            Text(text = "send text")
            //        }
        }

        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()) {
//            TextField(value = text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth())
            TextField(
                value = text,
                placeholder = { Text(text = stringResource(id = R.string.message))},
                onValueChange = {
                    text = it
                },
                shape = RoundedCornerShape(40.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            if (text.isNotEmpty()){
                                messagingViewModel.sendMessage(
                                    roomUid =chatRoomUid,
                                    senderUid =currentUserUid,
                                    textMessage = text
                                )
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(
                                id = R.string.send_message
                            )
                        )
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.99f)
            )
        }


    }
}