package com.example.close.data.location

import com.example.close.data.location.model.FriendLocationDetail
import com.example.close.data.location.model.LocationDetail
import com.example.close.data.location.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationSource {

    suspend fun fetchCurrentLocation(): Flow<LocationModel>

    suspend fun getFriendsLocation(userUID: String): Flow<List<FriendLocationDetail>>

    suspend fun getFriendsLocationNotFlow(userUID: String): List<FriendLocationDetail>

    suspend fun createLocationContainer(userUID: String)

    suspend fun shareLocation(friendUID: String, friendsLocationDetail: FriendLocationDetail)

    suspend fun checkIfLocationContainerContainer(userUID: String): Boolean

    suspend fun getLocationByUserUID(userUID: String): LocationDetail

    suspend fun setLocationDetail(userUID: String, locationDetail: LocationModel)

    suspend fun createLocationDocument(userUID: String)

    suspend fun getLocationByUserUIDFlow(userUID: String): Flow<LocationDetail>
}