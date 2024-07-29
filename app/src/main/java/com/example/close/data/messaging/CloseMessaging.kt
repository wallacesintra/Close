package com.example.close.data.messaging

import com.example.close.data.messaging.models.CloseChatRoom
import com.example.close.data.messaging.models.CloseMessage
import com.example.close.presentation.messaging.models.MessageUI
import kotlinx.coroutines.flow.Flow

interface CloseMessaging {

    suspend fun createChatRoom(closeChatRoom: CloseChatRoom)

    suspend fun sendMessage(roomUid: String,  senderUid: String,textMessage: String)

    suspend fun getChatRoomsForUid(userUid: String): List<CloseChatRoom>

    suspend fun deleteMessage(roomUid: String, message: MessageUI)

    suspend fun getChatRoomMessages(chatRoomUid: String): Flow<List<CloseMessage>>

}