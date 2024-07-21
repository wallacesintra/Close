package com.example.close.presentation.location.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
import com.example.close.data.location.model.LocationModel
import com.example.close.presentation.location.models.FriendLocation
import com.example.close.presentation.location.models.LocationDetails
import com.example.close.presentation.location.models.LocationState
import com.example.close.presentation.location.models.SharingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LocationViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val locationDataSource: LocationDataSource,
    private val closeUserDataSource: CloseUserDataSource,
): ViewModel() {

    init {
        viewModelScope.launch {
            val location = locationDataSource.getLocationByUserUIDFlow("Cuo4cDX5UCRXs4J4zOaIJ6PLP0d2")
            location.collect{value ->
                println(value)
                Log.d("location test viewmodel", value.locationDetail.latitude.toString())
            }
        }
    }


    var locationState: LocationState by mutableStateOf(LocationState.Loading)

    private val _location = MutableStateFlow<LocationModel?>(null)
    val location: StateFlow<LocationModel?> get() = _location

    var sharingState: SharingState by mutableStateOf(SharingState.Success(friendsLocationList = emptyList()))

//    private val _friendsLocation = locationDataSource.getFriendsLocation(userUID = )


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
     * update location detail
     * @param userUID : current user uid
     * @param locationDetail: location latitude and longitude
     */
    fun updateLocationDetails(userUID: String, friendsLIst: List<String>, locationDetail: LocationModel){


        viewModelScope.launch(Dispatchers.IO){
            delay(2000)
//            val userLocationDetail = FriendLocationDetail(
//                userUID = userUID,
//                locationDetail = locationDetail
//            )

            locationDataSource.setLocationDetail(
                userUID = userUID,
                locationDetail = locationDetail
            )

//            for (friendUID in friendsLIst){
//
//                Log.d("location sharing", "location fetched $friendUID")
//
//                locationDataSource.shareLocation(
//                    friendUID = friendUID,
//                    friendsLocationDetail = userLocationDetail
//                )
//
//                locationDataSource.setLocationDetail(
//                    userUID =  ,
//                    locationDetail =
//                )
//            }

        }
    }




    /**
     * get location from friends
     *
     * @param userUID current user uid
     * @param friendsLIst friend uid list
     */
    fun getFriendsLocationDetails(userUID: String, friendsLIst: List<String>) {
        Log.d("LocationSharing", "Attempting to receive locations for user $userUID")
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                delay(2000)

                val friendsLocationList = mutableListOf<FriendLocation>()

                friendsLIst.forEach { friendUID ->
                    val locationDetail = locationDataSource.getLocationByUserUID(userUID = friendUID)
                    val user = closeUserDataSource.getCloseUserByUid(closeUid = friendUID)

//                    val locationCoordinates = locationDataSource.getLocationByUserUIDFlow(userUID = friendUID)

                    Log.d("Getting Location detail", "location from $locationDetail")
                    Log.d("Getting Location detail", "location from $user")


                    friendsLocationList.add(
                        FriendLocation(
                            closerUser = closeUserDataSource.getCloseUserByUid(closeUid = friendUID),
                            locationCoordinates = locationDataSource.getLocationByUserUID(userUID = friendUID)
                        )
                    )
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
                    savedStateHandle = savedStateHandle,
                    locationDataSource = locationDataSource,
                    closeUserDataSource = closeUserDataSource
                )
            }
        }
    }
}