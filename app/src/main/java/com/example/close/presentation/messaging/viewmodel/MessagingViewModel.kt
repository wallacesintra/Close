package com.example.close.presentation.messaging.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.database.models.CloseUser
import com.example.close.data.messaging.CloseMessagingDataSource
import com.example.close.presentation.messaging.models.ChatRoomsState
import com.example.close.presentation.messaging.models.CloseChatRoomUI
import com.example.close.presentation.messaging.models.MessageListUI
import com.example.close.presentation.messaging.models.MessageType
import com.example.close.presentation.messaging.models.MessageUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MessagingViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val closeMessagingDataSource: CloseMessagingDataSource,
    private val closeUserDataSource: CloseUserDataSource
): ViewModel() {

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()


    private val _showMessages  = MutableStateFlow(MessageType.ALL)

    fun setChatRoomUID(chatRoomUID: String){
        savedStateHandle["chatRoomUID"] = chatRoomUID
    }

    fun resetChatRoomUID(){
        savedStateHandle["chatRoomUID"] = ""
    }

    private val chatRoomUID = savedStateHandle.getStateFlow("chatRoomUID","")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _showMessageList = _showMessages
        .flatMapLatest { show ->
            chatRoomUID.let { uid ->
                when(show){
                    MessageType.ALL -> closeMessagingDataSource.getChatRoomMessages(chatRoomUid = uid.value)
                    MessageType.UNREAD -> closeMessagingDataSource.getChatRoomMessages(chatRoomUid = uid.value)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _messageListState = MutableStateFlow(MessageListUI())

    val showMessageList = combine(_messageListState, _showMessages, _showMessageList){ state, _, list ->
        val messageList = mutableListOf<MessageUI>()

        for (i in list.reversed()){
            val sender = closeUserDataSource.getCloseUserByUid(i.senderUid)
            messageList.add(
                MessageUI(
                    message = i.message,
                    sender = sender,
                    messageUid = i.messageUid
                )
            )
        }

        state.copy(
            messageList = messageList,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MessageListUI())



    var chatRoomsState: ChatRoomsState by mutableStateOf(ChatRoomsState.Success(chatRoomList = emptyList()))

    fun messageTextChange(text: String) {
        _messageText.value = text
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(roomUid: String, senderUid: String, textMessage:String){
        viewModelScope.launch(Dispatchers.IO) {
            closeMessagingDataSource.sendMessage(roomUid, senderUid, textMessage)
            _messageText.value = ""
        }
    }

    fun getCurrentUserChatRooms(currentUserUid: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {

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


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val closeMessagingDataSource = application.container.closeMessagingDataSource
                val closeUserDataSource = application.container.closeUserDataSource

                val savedStateHandle = createSavedStateHandle()

                MessagingViewModel(savedStateHandle = savedStateHandle,closeMessagingDataSource = closeMessagingDataSource, closeUserDataSource = closeUserDataSource)
            }
        }
    }



}