package com.example.close.data.cometChat

import com.example.close.data.cometChat.models.CometAuthDto

interface CometChatAuth {
    suspend fun getAuthToken(userId: String): CometAuthDto

    suspend fun createCometUser(userId: String, username: String)

    suspend fun logInUser(userId: String)

    suspend fun logOutUser()

}