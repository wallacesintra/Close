package com.example.close.data.database

import com.example.close.data.database.models.CloseUser
import com.example.close.data.database.models.CloseUserData
import com.example.close.data.database.models.FriendRequest

interface CloseUserSource {
    suspend fun addNewCloseUser(newUser: CloseUserData)

    suspend fun getSignedInUser(uid: String): CloseUserData

//    suspend fun getSignedInUserFlow(uid: String): CloseUserData

    suspend fun updateDetail(detailToUpdate: String,userUid: String, newValue: String)

    suspend fun findUserByUsername(username: String): List<CloseUser>

    suspend fun getCloseUserByUid(closeUid: String): CloseUser

    suspend fun addFriend(closeUid: String, newFriendUid: String)

    suspend fun sendFriendRequest(senderUid: String, receiverUid: String)

    suspend fun getFriendRequests(receiverUid: String): List<FriendRequest>

    suspend fun acceptFriendRequest(requestUid: String)

}