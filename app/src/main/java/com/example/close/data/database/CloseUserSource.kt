package com.example.close.data.database

import com.example.close.presentation.models.CloseUserData

interface CloseUserSource {
    suspend fun addNewCloseUser(newUser: CloseUserData)

    suspend fun getSignInUser(uid: String): CloseUserData

    suspend fun updateDetail(detailToUpdate: String,userUid: String, newValue: String)

}