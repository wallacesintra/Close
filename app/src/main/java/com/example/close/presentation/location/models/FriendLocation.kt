package com.example.close.presentation.location.models

import com.example.close.data.database.models.CloseUser
import com.example.close.data.location.model.LocationModel

data class FriendLocation(
    val closerUser: CloseUser = CloseUser(),
    val locationCoordinates: LocationModel? = null
)