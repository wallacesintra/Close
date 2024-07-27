package com.example.close.presentation.messaging.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.LargeText
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.messaging.components.ChatBubble
import com.example.close.presentation.messaging.viewmodel.MessagingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleChatRoom(
    chatRoomUid: String,
    currentUserUid: String,
    messagingViewModel: MessagingViewModel
) {
    val messageText by messagingViewModel.messageText.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        messagingViewModel.setChatRoomUID(chatRoomUID = chatRoomUid)
        listState.scrollToItem(index = 0)
    }

    DisposableEffect(true) {
        onDispose {
            messagingViewModel.resetChatRoomUID()
        }
    }

    val showMessageList by messagingViewModel.showMessageList.collectAsState()

    Box(modifier = Modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {

            LargeText(
                text = stringResource(id = R.string.messages),
                modifier = Modifier.padding(vertical = 10.dp)
            )

            
            if (showMessageList.messageList.isEmpty()){
                MediumText(
                    text = stringResource(id = R.string.say_hi),
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterHorizontally)
                )
            }
            LazyColumn(
                state = listState,
                reverseLayout = true,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(bottom = 70.dp)
            ) {
                items(
                    items = showMessageList.messageList,
                    key = { message -> message.messageUid }
                ){ message ->
                    ChatBubble(currentUserUid = currentUserUid, messageUI = message)
                }
            }
            
        }

        Box(
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            TextField(
                value = messageText,
                placeholder = { Text(text = stringResource(id = R.string.message))},
                shape = RoundedCornerShape(40.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = messagingViewModel::messageTextChange,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            if (messageText.isNotEmpty()) {
                                messagingViewModel.sendMessage(
                                    roomUid = chatRoomUid,
                                    senderUid = currentUserUid,
                                    textMessage = messageText
                                )
                            }


                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.secondary
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
