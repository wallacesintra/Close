package com.example.close.presentation.friends.viewmodels

import android.util.Log
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
import com.example.close.data.messaging.CloseMessagingDataSource
import com.example.close.data.messaging.models.CloseChatRoom
import com.example.close.presentation.friends.models.CloseFriendRequest
import com.example.close.presentation.friends.models.FriendRequestsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendRequestsViewModel(
    private val closeUserDataSource: CloseUserDataSource,
    private val closeMessagingDataSource: CloseMessagingDataSource
): ViewModel() {

    var friendRequestsState : FriendRequestsState by mutableStateOf(FriendRequestsState.Loading)

    fun sendFriendRequest(senderUid:String, receiverUid: String){
        viewModelScope.launch(Dispatchers.IO) {
            if (senderUid.isNotBlank() && receiverUid.isNotBlank()){
                closeUserDataSource.sendFriendRequest(senderUid = senderUid, receiverUid = receiverUid)
                closeUserDataSource.addFriend(closeUid = senderUid, newFriendUid = receiverUid)
                Log.d("send request", "send button clicked")
            }
        }
    }

    fun acceptFriendRequest(closeFriendRequest: CloseFriendRequest, currentUserUid: String){
        viewModelScope.launch(Dispatchers.IO) {
            closeUserDataSource.acceptFriendRequest(requestUid = closeFriendRequest.requestUid)

            closeUserDataSource.addFriend(closeUid = currentUserUid, newFriendUid = closeFriendRequest.senderUid)

            val newCloseChatRoom = CloseChatRoom(
                chatUid = closeFriendRequest.requestUid,
                members = listOf(closeFriendRequest.senderUid, currentUserUid),
                messages = emptyList()
            )
            closeMessagingDataSource.createChatRoom(newCloseChatRoom)
        }
    }



    fun getCurrentUserFriendRequests(currentUserUid: String){
        viewModelScope.launch(Dispatchers.IO) {

            try {
                friendRequestsState = FriendRequestsState.Loading

                val requestList = closeUserDataSource.getFriendRequests(receiverUid = currentUserUid)

                val closeFriendRequestList = requestList.map { request ->
                    val sender = closeUserDataSource.getCloseUserByUid(closeUid = request.senderUid)

                    CloseFriendRequest(
                        requestUid = request.requestUid,
                        senderUid = sender.uid,
                        senderUsername = sender.username,
                        response = request.accepted
                    )
                }

                friendRequestsState = try {

                    FriendRequestsState.Success(
                        requestList = closeFriendRequestList.filter { request ->
                            request.response == null
                        }
                    )

                }catch (e: Exception){
                    Log.w("receive friend request", "failed : $e")
                    FriendRequestsState.Error(
                        errorMessage = e.message!!
                    )
                }

            }catch (e: Exception){
                Log.w("receive friend request", "failed : $e" )
                friendRequestsState = FriendRequestsState.Error(
                    errorMessage = e.message!!
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val closeUserDataSource = application.container.closeUserDataSource
                val closeMessagingDataSource = application.container.closeMessagingDataSource

                FriendRequestsViewModel(
                    closeUserDataSource = closeUserDataSource,
                    closeMessagingDataSource = closeMessagingDataSource
                )
            }
        }
    }
}