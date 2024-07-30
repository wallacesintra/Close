package com.example.close.presentation.messaging.models

import com.example.close.data.users.models.CloseUser
import com.example.close.data.messaging.models.CloseMessage

data class CloseChatRoomUI(
    val chatUid: String = "",
    val members: List<CloseUser> = emptyList(),
    val messages: List<CloseMessage> = emptyList()
)