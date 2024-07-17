package com.example.close.presentation.location.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.location.LocationDataSource
import com.example.close.data.location.model.FriendLocationDetail
import com.example.close.data.location.model.LocationModel
import com.example.close.presentation.location.models.FriendLocation
import com.example.close.presentation.location.models.LocationDetails
import com.example.close.presentation.location.models.LocationState
import com.example.close.presentation.location.models.SharingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LocationViewModel(
    private val locationDataSource: LocationDataSource,
    private val closeUserDataSource: CloseUserDataSource,
): ViewModel() {


    var locationState: LocationState by mutableStateOf(LocationState.Loading)

    private val _location = MutableStateFlow<LocationModel?>(null)
    val location: StateFlow<LocationModel?> get() = _location

    var sharingState: SharingState by mutableStateOf(SharingState.Success(friendsLocationList = emptyList()))


//    fun getCurrentUserUID(userUID: String){
//        savedStateHandle["currentUserUID"] = userUID
//    }
//
//    private val currentUserUID = savedStateHandle.getStateFlow("currentUserUID", "")

//    init {
//        getCurrentLocation()
//    }

    fun createLocationContainer(userUID: String){
        viewModelScope.launch(Dispatchers.IO) {
            if(!locationDataSource.checkIfLocationContainerContainer(userUID = userUID)){
                locationDataSource.createLocationContainer(userUID = userUID)
            }
        }
    }

    fun getCurrentLocation(){
        viewModelScope.launch(Dispatchers.IO) {

            Log.d("location fetched: loading", "loading")

            try {
                locationState = LocationState.Loading

                val locationData = locationDataSource.fetchCurrentLocation().first()

                locationState = try {
                    Log.d("location fetched:successful", "successful")

                    LocationState.Success(
                        locationDetails = LocationDetails(
                            lat = locationData.latitude,
                            long = locationData.longitude
                        )
                    )

                }catch (e: Exception){
                    Log.w("location fetched:failure", "failure" + e.message)
                    LocationState.Error(error = e.message!!)
                }

            }catch (e: Exception){
                Log.w("location fetched:failure", "failure " + e.message)

                LocationState.Error(error = e.message!!)
            }

        }
    }




    /**
     * sharing location to user's friends
     * @param userUID : current user uid
     * @param friendsLIst: friends uid list
     * @param locationDetail: location latitude and longitude
     */
    fun shareLocationToFriends(userUID: String,  friendsLIst: List<String>,locationDetail: LocationModel){


        viewModelScope.launch(Dispatchers.IO){
            val userLocationDetail = FriendLocationDetail(
                userUID = userUID,
                locationDetail = locationDetail
            )

            for (friendUID in friendsLIst){

                Log.d("location sharing", "location fetched $friendUID")

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
    fun receiveLocationsFromFriends(userUID: String) {
        Log.d("LocationSharing", "Attempting to receive locations for user $userUID")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val friendsLocationList = mutableListOf<FriendLocation>()
                val locationList = locationDataSource.getFriendsLocationNotFlow(userUID = userUID)
                locationList.forEach { location ->
                    closeUserDataSource.getCloseUserByUid(closeUid = location.userUID).let { user ->
                        friendsLocationList.add(
                            FriendLocation(
                                closerUser = user,
                                locationCoordinates = location.locationDetail
                            )
                        )
                    }
                }
                Log.d("LocationSharing", "Successfully received ${friendsLocationList.size} locations")
                sharingState = SharingState.Success(friendsLocationList = friendsLocationList)
            } catch (e: Exception) {
                Log.e("LocationSharing", "Error receiving locations: ${e.message}", e)
                sharingState = SharingState.Error(error = e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val locationDataSource = application.container.locationDataSource
                val closeUserDataSource = application.container.closeUserDataSource
                val savedStateHandle = createSavedStateHandle()


                LocationViewModel(
                    locationDataSource = locationDataSource,
                    closeUserDataSource = closeUserDataSource
                )
            }
        }
    }
}