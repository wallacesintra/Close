package com.example.close.data

import android.content.Context
import com.example.close.data.auth.UserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

interface AppContainer {
    val userDataSource: UserDataSource
}

class DefaultContainer(
    private val applicationContext: Context,
    private val auth: FirebaseAuth
): AppContainer {

    override val userDataSource: UserDataSource by lazy {
        UserDataSource(
            context = applicationContext,
            auth = auth
        )
    }

}