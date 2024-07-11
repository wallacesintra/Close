package com.example.close.data.messaging

import com.example.close.data.messaging.models.CloseChatRoom
import com.example.close.data.messaging.models.CloseMessage
import kotlinx.coroutines.flow.Flow

interface CloseMessaging {

    suspend fun createChatRoom(closeChatRoom: CloseChatRoom)

    suspend fun sendMessage(roomUid: String,  senderUid: String,textMessage: String)

//    suspend fun getChatRoomByChatRoomUid(chatroomUid: String): CloseChatRoom

//    suspend fun getChatRoomByChatRoomUid(chatroomUid: String): Flow<CloseChatRoom>

    suspend fun getChatRoomsForUid(userUid: String): List<CloseChatRoom>

//    suspend fun getChatRoomMessages(chatRoomUID: String): List<CloseMessage>

    suspend fun getChatRoomMessages(chatRoomUid: String): Flow<List<CloseMessage>>

}