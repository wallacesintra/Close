package com.example.close.presentation.messaging.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.database.models.CloseUser
import com.example.close.data.messaging.CloseMessagingDataSource
import com.example.close.presentation.messaging.models.ChatRoomsState
import com.example.close.presentation.messaging.models.CloseChatRoomUI
import com.example.close.presentation.messaging.models.MessageState
import com.example.close.presentation.messaging.models.MessageUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagingViewModel(
    private val closeMessagingDataSource: CloseMessagingDataSource,
    private val closeUserDataSource: CloseUserDataSource
): ViewModel() {

    var chatRoomsState: ChatRoomsState by mutableStateOf(ChatRoomsState.Loading)
    var messageState: MessageState by mutableStateOf(MessageState.Loading)

    fun sendMessage(roomUid: String, senderUid: String, textMessage:String){
        viewModelScope.launch(Dispatchers.IO) {
            closeMessagingDataSource.sendMessage(roomUid, senderUid, textMessage)
        }
    }

    fun getCurrentUserChatRooms(currentUserUid: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRoomsState = ChatRoomsState.Loading

                val chatRoomList = closeMessagingDataSource.getChatRoomsForUid(userUid = currentUserUid)
                val closeChatRoomLIstUI = mutableListOf<CloseChatRoomUI>()

                for (room in chatRoomList){
                    val membersList = mutableListOf<CloseUser>()

                    for (member in room.members){
                        val closeUser = closeUserDataSource.getCloseUserByUid(member)
                        membersList.add(closeUser)
                    }

                    val chatRoom = CloseChatRoomUI(
                        chatUid = room.chatUid ,
                        members = membersList,
                        messages = room.messages
                    )

                    closeChatRoomLIstUI.add(chatRoom)

                }

                chatRoomsState = try {
                    ChatRoomsState.Success(chatRoomList = closeChatRoomLIstUI)
                }catch (e: Exception){
                    ChatRoomsState.Error(e.message!!)
                }
            }catch (e: Exception){
                chatRoomsState = ChatRoomsState.Error(errorMessage = e.message!!)
            }
        }
    }

    fun getChatRoomByChatUid(chatRoom: String){
        viewModelScope.launch {
            try {

                messageState = MessageState.Loading

                val currentChatRoom = closeMessagingDataSource.getChatRoomByChatRoomUid(chatroomUid = chatRoom)

                val messagesList = mutableListOf<MessageUI>()

                for (message in currentChatRoom.messages){
                    val sender = closeUserDataSource.getCloseUserByUid(message.senderUid)

                    messagesList.add(
                        MessageUI(
                            messageUid = message.messageUid,
                            sender = sender,
                            message = message.message,
                        )
                    )
                }

                messageState = try {
                    MessageState.Success(messagesList = messagesList)
                }catch (e: Exception){
                    MessageState.Error(e.message!!)
                }
            }catch (e: Exception){
                messageState = MessageState.Error(error = e.message!!)
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val closeMessagingDataSource = application.container.closeMessagingDataSource
                val closeUserDataSource = application.container.closeUserDataSource

                MessagingViewModel(closeMessagingDataSource = closeMessagingDataSource, closeUserDataSource = closeUserDataSource)
            }
        }
    }

}