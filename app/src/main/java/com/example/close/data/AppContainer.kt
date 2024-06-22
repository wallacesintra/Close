package com.example.close.data

import android.content.Context
import com.example.close.data.auth.UserDataSource
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.location.LocationDataSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val userDataSource: UserDataSource
    val closeUserDataSource: CloseUserDataSource
    val locationDataSource: LocationDataSource
}

class DefaultContainer(
    private val applicationContext: Context,
    private val auth: FirebaseAuth,
    private val firestoreDb: FirebaseFirestore

): AppContainer {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)


    override val userDataSource: UserDataSource by lazy {
        UserDataSource(
            auth = auth
        )
    }

    override val closeUserDataSource: CloseUserDataSource by lazy {
        CloseUserDataSource(
            firestoreDb = firestoreDb
        )
    }


    override val locationDataSource: LocationDataSource by lazy {
        LocationDataSource(
            context = applicationContext,
            fusedLocationProviderClient = fusedLocationClient,
            currentLocationRequest = LocationRequest.create()
        )
    }


}