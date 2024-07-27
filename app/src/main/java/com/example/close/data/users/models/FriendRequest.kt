package com.example.close.data.users.models

data class FriendRequest(
    val requestUid: String = "",
    val receiverUid: String = "",
    val senderUid: String = "",
    var accepted: Boolean? = null
)
