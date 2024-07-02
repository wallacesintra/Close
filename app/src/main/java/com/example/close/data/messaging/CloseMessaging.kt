package com.example.close.data.messaging

import com.example.close.data.messaging.models.CloseChatRoom

interface CloseMessaging {

    suspend fun createChatRoom(closeChatRoom: CloseChatRoom)

    suspend fun sendMessage(roomUid: String,  senderUid: String,textMessage: String)

    suspend fun getChatRoomByChatRoomUid(chatroomUid: String): CloseChatRoom

    suspend fun getChatRoomsForUid(userUid: String): List<CloseChatRoom>


}