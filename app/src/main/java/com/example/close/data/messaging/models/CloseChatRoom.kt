package com.example.close.data.messaging.models

data class CloseChatRoom(
    val chatUid: String = "",
    val members: List<String> = emptyList(),
    val messages: List<CloseMessage> = emptyList(),
)