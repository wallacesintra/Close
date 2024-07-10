package com.example.close.presentation.location.models

import com.example.close.data.database.models.CloseUser
import com.google.android.gms.maps.model.LatLng

data class FriendLocation(
    val closerUser: CloseUser = CloseUser(),
    val locationCoordinates: LatLng? = null
)