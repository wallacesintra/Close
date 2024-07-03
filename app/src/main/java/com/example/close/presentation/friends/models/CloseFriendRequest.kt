package com.example.close.presentation.friends.models

data class CloseFriendRequest(
    val requestUid: String = "",
    val senderUid: String = "",
    val senderUsername: String = "",
    var response: Boolean? = null
)