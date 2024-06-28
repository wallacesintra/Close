package com.example.close.data.cometChat.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CometAuthDto(
    @Contextual val data: Data
)

@Serializable
data class Data(
    val uid: String,
    val authToken: String,
    val createdAt: Int
)