package com.example.close.presentation.messaging.models

sealed interface ChatRoomsState {
    data class Success(val chatRoomList: List<CloseChatRoomUI>): ChatRoomsState

    data class Error(val errorMessage: String): ChatRoomsState

    data object Loading: ChatRoomsState

}