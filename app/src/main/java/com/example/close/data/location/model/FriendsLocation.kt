package com.example.close.data.location.model

data class FriendsLocation(
    val userUID: String = "",
    val friendsLocationList: List<FriendLocationDetail> = emptyList()
)


