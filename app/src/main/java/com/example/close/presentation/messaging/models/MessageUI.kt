package com.example.close.presentation.messaging.models

import com.example.close.data.database.models.CloseUser

data class MessageUI(
    val messageUid: String = "",
    val sender: CloseUser = CloseUser(),
    val message: String = ""
)