package com.example.close.data.location.model

data class FriendsLocation(
    val userUID: String = "",
    val friendsLocationList: List<FriendLocationDetail> = emptyList()
)


data class FriendLocationDetail(
    val userUID: String = "",
    val locationDetail: LocationModel? = null
)