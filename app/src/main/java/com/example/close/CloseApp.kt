package com.example.close

import android.app.Application
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


    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        val db = Firebase.firestore

        container = DefaultContainer(
            applicationContext = this,
            auth = auth,
            firestoreDb = db
        )

        // Initialize the Google Maps SDK
        MapsInitializer.initialize(this)
    }
}