package com.example.close.presentation.messaging.models

import com.example.close.data.users.models.CloseUser
import com.google.errorprone.annotations.Immutable

@Immutable
data class MessageUI(
    val messageUid: String = "",
    val sender: CloseUser = CloseUser(),
    val message: String = ""
)