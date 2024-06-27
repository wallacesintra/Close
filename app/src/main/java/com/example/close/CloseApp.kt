package com.example.close

import android.app.Application
import android.util.Log
import com.cometchat.chat.core.AppSettings
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.example.close.data.AppContainer
import com.example.close.data.DefaultContainer
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class CloseApp: Application(){
    lateinit var container: AppContainer
    lateinit var auth: FirebaseAuth


    private val cometAppID = "2598215a4c67662c"
    private val region = "us"
    private val REST_API_KEY = "b6f034fe78f65b0ca65c8bdcce970ed4fcc62ddf"

    private val appSetting = AppSettings.AppSettingsBuilder()
        .subscribePresenceForAllUsers()
        .setRegion(region)
        .autoEstablishSocketConnection(true)
        .build()

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        val db = Firebase.firestore


        CometChat.init(
            this,
            cometAppID,
            appSetting,
            object : CometChat.CallbackListener<String>(){
                override fun onSuccess(closeAuth: String?) {
                    Log.d("CometChat Initialization: successful", "Initialization successful")
                }

                override fun onError(p0: CometChatException?) {
                    Log.e("CometChat Initialization: successful", "Initialization failed with exception: " + p0?.message)
                }
            }

        )


        container = DefaultContainer(
            applicationContext = this,
            auth = auth,
            firestoreDb = db,
            cometAppID = cometAppID,
            cometRegion = region,
            cometRestApiKey = REST_API_KEY
        )

        // Initialize the Google Maps SDK
        MapsInitializer.initialize(this)
    }
}