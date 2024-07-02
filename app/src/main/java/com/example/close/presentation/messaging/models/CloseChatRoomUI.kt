package com.example.close.presentation.messaging.models

import com.example.close.data.database.models.CloseUsers
import com.example.close.data.messaging.models.CloseMessage

data class CloseChatRoomUI(
    val chatUid: String = "",
    val members: List<CloseUsers> = emptyList(),
    val messages: List<CloseMessage> = emptyList()
)