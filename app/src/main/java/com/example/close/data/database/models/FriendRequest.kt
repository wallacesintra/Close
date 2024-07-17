package com.example.close.data.database.models

import com.google.firebase.firestore.FieldValue

data class FriendRequest(
    val requestUid: String = "",
    val receiverUid: String = "",
    val senderUid: String = "",
    var accepted: Boolean? = null
)
