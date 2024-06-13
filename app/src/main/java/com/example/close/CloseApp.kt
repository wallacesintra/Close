package com.example.close

import android.app.Application
import com.example.close.data.AppContainer
import com.example.close.data.DefaultContainer
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CloseApp: Application(){
    lateinit var container: AppContainer
    lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth

        container = DefaultContainer(
            applicationContext = this,
            auth = auth
        )
    }
}