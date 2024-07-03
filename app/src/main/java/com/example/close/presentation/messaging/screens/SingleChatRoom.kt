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
import com.example.close.presentation.components.LargeText
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

    var message: String by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        messagingViewModel.getChatRoomByChatUid(chatRoom = chatRoomUid)
    }

    Box(modifier = Modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {

            LargeText(text = stringResource(id = R.string.messages), modifier = Modifier.padding(vertical = 10.dp))

            when (messageState) {
                is MessageState.Error -> {
                    Text(text = messageState.error)
                }

                MessageState.Loading -> {
                    Loading()
                }

                is MessageState.Success -> {
                    val list = messageState.messagesList
                    LazyColumn(
                        reverseLayout = true,
                        modifier = Modifier
                            .weight(1.0f)
                            .padding(bottom = 70.dp)
                    ) {
                        items(list) { message ->
                            ChatBubble(currentUserUid = currentUserUid, messageUI = message)
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier
            .padding(5.dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()) {
            TextField(
                value = message,
                placeholder = { Text(text = stringResource(id = R.string.message))},
                onValueChange = {
                    message = it
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
                            if (message.isNotEmpty()){
                                messagingViewModel.sendMessage(
                                    roomUid =chatRoomUid,
                                    senderUid =currentUserUid,
                                    textMessage = message
                                )
                            }

                            message = ""
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