package com.example.close.presentation.messaging.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.messaging.CloseMessagingDataSource
import com.example.close.data.users.CloseUserDataSource
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

@OptIn(ExperimentalCoroutinesApi::class)
class SingleChatRoomViewModel(
    private val closeUserDataSource: CloseUserDataSource,
    private val closeMessagingDataSource: CloseMessagingDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messages = MutableStateFlow<List<MessageUI>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    fun setChatRoomUID(chatRoomUID: String){
        savedStateHandle["chatRoomUID"] = chatRoomUID
    }

    private fun resetChatRoomUID(){
        savedStateHandle["chatRoomUID"] = ""
    }

    override fun onCleared() {
        super.onCleared()
        resetChatRoomUID()
    }

    fun messageTextChange(text: String) {
        _messageText.value = text
    }

    fun sendMessage(roomUid: String, senderUid: String, textMessage:String){
        viewModelScope.launch(Dispatchers.IO) {
            closeMessagingDataSource.sendMessage(roomUid, senderUid, textMessage)
            _messageText.value = ""
        }
    }

    fun deleteMessage(roomUid: String, message: MessageUI){
        viewModelScope.launch(Dispatchers.IO) {
            closeMessagingDataSource.deleteMessage(roomUid = roomUid, message = message)
        }
    }

    private val chatUID = savedStateHandle.getStateFlow("chatRoomUID","")



    private val _showMessages  = MutableStateFlow(MessageType.ALL)


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _showMessageList = _showMessages
        .flatMapLatest { show ->
            chatUID.let { uid ->
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




//    init {
//        viewModelScope.launch(Dispatchers.IO) {
////            val chatUID = savedStateHandle.get<String>("chatRoomUID")
//            val chatUID = savedStateHandle.getStateFlow("chatRoomUID","")
//            val messagesList = mutableListOf<MessageUI>()
//
//            chatUID.let { uid ->
//                closeMessagingDataSource.getChatRoomMessages(uid.value).collect{chats ->
//                    chats.forEach { chat ->
//                        messagesList.add(
//                            MessageUI(
//                                message = chat.message,
//                                messageUid = chat.messageUid,
//                                sender = closeUserDataSource.getCloseUserByUid(chat.senderUid)
//                            )
//                        )
//                    }
//                }
//            }
//
////            closeMessagingDataSource.getChatRoomMessages(chatUID).collect{chats ->
////                chats.forEach { item ->
////                    messagesList.add(
////                        MessageUI(
////                            messageUid = item.messageUid,
////                            message = item.message,
////                            sender = closeUserDataSource.getCloseUserByUid(item.senderUid)
////                        )
////                    )
////                }
////            }
//
//            _messages.update { messagesList }
//        }
//    }




    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)

                val closeMessagingDataSource = application.container.closeMessagingDataSource
                val closeUserDataSource = application.container.closeUserDataSource
                val savedStateHandle = createSavedStateHandle()

                SingleChatRoomViewModel(
                    closeUserDataSource =closeUserDataSource,
                    closeMessagingDataSource =closeMessagingDataSource,
                    savedStateHandle =savedStateHandle
                )
            }
        }
    }

}