package com.example.close.data.database

import com.example.close.presentation.models.CloseUserData

interface CloseUserSource {
    suspend fun addNewCloseUser(newUser: CloseUserData)
}