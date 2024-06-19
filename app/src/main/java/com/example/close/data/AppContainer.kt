package com.example.close.data

import android.content.Context
import com.example.close.data.auth.UserDataSource
import com.example.close.data.database.CloseUserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val userDataSource: UserDataSource
    val closeUserDataSource: CloseUserDataSource
}

class DefaultContainer(
    private val applicationContext: Context,
    private val auth: FirebaseAuth,
    private val firestoreDb: FirebaseFirestore

): AppContainer {

    override val userDataSource: UserDataSource by lazy {
        UserDataSource(
            context = applicationContext,
            auth = auth
        )
    }

    override val closeUserDataSource: CloseUserDataSource by lazy {
        CloseUserDataSource(
            context = applicationContext,
            firestoreDb = firestoreDb
        )
    }

}