package com.example.close.presentation.location.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.location.LocationDataSource
import com.example.close.data.location.model.FriendLocationDetail
import com.example.close.presentation.location.models.FriendLocation
import com.example.close.presentation.location.models.SharingState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharingLocationViewModel(
    private val closeUserDataSource: CloseUserDataSource,
    private val locationDataSource: LocationDataSource
): ViewModel() {

    var sharingState: SharingState by mutableStateOf(SharingState.Success(friendsLocationList = emptyList()))




    /**
     * sharing location to user's friends
     * @param userUID : current user uid
     * @param friendsLIst: friends uid list
     * @param locationDetail: location latitude and longitude
     */
    fun shareLocationToFriends(userUID: String,  friendsLIst: List<String>,locationDetail: LatLng){

        viewModelScope.launch(Dispatchers.IO){
            val userLocationDetail = FriendLocationDetail(
                userUID = userUID,
                locationDetail = locationDetail
            )

            for (friendUID in friendsLIst){
                locationDataSource.shareLocation(
                    friendUID = friendUID,
                    friendsLocationDetail = userLocationDetail
                )
            }

        }
    }


    /**
     * get location from friends
     *
     * @param userUID current user uid
     */
    fun receiveLocationsFromFriends(userUID: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {

                sharingState = try {
                    val friendsLocationList = mutableListOf<FriendLocation>()

                    locationDataSource.getFriendsLocation(userUID = userUID).collect { list ->
                        for(i in list){
                            val user = closeUserDataSource.getCloseUserByUid(i.userUID)

                            friendsLocationList.add(
                                FriendLocation(
                                    closerUser = user,
                                    locationCoordinates = i.locationDetail!!
                                )
                            )
                        }
                    }

                    SharingState.Success(friendsLocationList = friendsLocationList)

                }catch (e: Exception){
                    SharingState.Error(e.message!!)
                }
            }catch (e: Exception){
                sharingState = SharingState.Error(error = e.message!!)
            }
        }
    }





    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val locationDataSource = application.container.locationDataSource
                val closeUserDataSource = application.container.closeUserDataSource

                SharingLocationViewModel(
                    closeUserDataSource = closeUserDataSource,
                    locationDataSource = locationDataSource
                )
            }
        }
    }
}