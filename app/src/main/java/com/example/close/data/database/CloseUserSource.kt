package com.example.close.data.database

import com.example.close.data.database.models.CloseUsers
import com.example.close.data.database.models.FriendRequest
import com.example.close.presentation.models.CloseUserData

interface CloseUserSource {
    suspend fun addNewCloseUser(newUser: CloseUserData)

    suspend fun getSignedInUser(uid: String): CloseUserData

    suspend fun updateDetail(detailToUpdate: String,userUid: String, newValue: String)

    suspend fun findUserByUsername(username: String): List<CloseUsers>

    suspend fun getCloseUserByUid(closeUid: String): CloseUsers

    suspend fun addFriend(closeUid: String, newFriendUid: String)


    suspend fun sendFriendRequest(senderUid: String, receiverUid: String)

    suspend fun getFriendRequests(receiverUid: String): List<FriendRequest>

    suspend fun acceptFriendRequest(requestUid: String)

}