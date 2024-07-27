package com.example.close.presentation.friends.models

import com.example.close.data.users.models.CloseUser

data class CloseFriendRequest(
    val requestUid: String = "",
    val senderUid: String = "",
    val senderCloseUserDetails: CloseUser = CloseUser(),
    var response: Boolean? = null
)