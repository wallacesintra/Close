package com.example.close.data.location.model

import com.google.android.gms.maps.model.LatLng

data class FriendsLocation(
    val userUID: String = "",
    val friendsLocationList: List<FriendLocationDetail> = emptyList()
)


data class FriendLocationDetail(
    val userUID: String = "",
    val locationDetail: LatLng? = null
)