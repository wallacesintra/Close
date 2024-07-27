package com.example.close.presentation.location.models

import com.example.close.data.users.models.CloseUser
import com.example.close.data.location.model.LocationDetail

data class FriendLocation(
    val closerUser: CloseUser = CloseUser(),
    val locationCoordinates: LocationDetail? = null,
    val distanceBetweenCurrentUserLocation: String = "0.0 km"
)