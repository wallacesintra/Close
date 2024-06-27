package com.example.close.data.database.models

import com.google.firebase.firestore.FieldValue

data class FriendRequest(
    val requestUid: String = "",
    val receiverUid: String = "",
    val senderUid: String = "",
//    val timeStamp: FieldValue,
//    val timeStamp: Long? = null,
    var accepted: Boolean? = null
)