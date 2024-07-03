package com.example.close.presentation.messaging.models

sealed interface MessageState {

    data class Success(val messagesList: List<MessageUI>): MessageState

    data class Error(val error: String): MessageState

    data object Loading: MessageState
}