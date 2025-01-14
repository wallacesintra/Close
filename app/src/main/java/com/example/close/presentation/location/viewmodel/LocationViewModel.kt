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
import com.example.close.data.location.LocationDataSource
import com.example.close.data.location.model.LocationModel
import com.example.close.data.users.CloseUserDataSource
import com.example.close.presentation.location.models.FriendLocation
import com.example.close.presentation.location.models.LocationDetails
import com.example.close.presentation.location.models.LocationState
import com.example.close.presentation.location.models.SharingState
import com.example.close.utils.CryptoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val locationDataSource: LocationDataSource,
    private val closeUserDataSource: CloseUserDataSource,
): ViewModel() {

    private val cryptoManager = CryptoManager()


    var locationState: LocationState by mutableStateOf(LocationState.Loading)

    private val _location = MutableStateFlow<LocationModel?>(null)
    val location: StateFlow<LocationModel?> get() = _location

    var sharingState: SharingState by mutableStateOf(SharingState.Success(friendsLocationList = emptyList()))


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
    fun updateLocationDetails(userUID: String, locationDetail: LocationModel){
        viewModelScope.launch(Dispatchers.IO){
            delay(500)

            Log.d("encrypt data", "real data : ${locationDetail.latitude}")
            val (encryptedLatitude, lat_iv) = cryptoManager.encryptDouble(locationDetail.latitude)

            Log.d("iv latitude", lat_iv)
            val (encryptedLongitude, long_iv) = cryptoManager.encryptDouble(locationDetail.longitude)

            val latitudePairing = Pair(encryptedLatitude, lat_iv)
            val longitudePairing = Pair(encryptedLongitude, long_iv)

//            Log.d("encrypt data", "decrypted data: $encryptedLatitude$iv")
            Log.d("encrypt data", "decrypted data: $latitudePairing")

//            Log.d("encrypt data", "encrypted data: ${cryptoManager.decryptDouble(encryptedLatitude, iv)}")
            Log.d("encrypt data", "encrypted data: ${cryptoManager.decryptDouble(latitudePairing.first, latitudePairing.second)}")


            locationDataSource.setLocationDetail(
                userUID = userUID,
                locationDetail = locationDetail
            )

//            val newLocationEncrypted = LocationModelEncrypted(
//                latitude = encryptedLatitude,
//                latitudeIv = lat_iv,
//                longitude = encryptedLongitude,
//                longitudeIv = long_iv
////                latitude = latitudePairing,
////                longitude = longitudePairing
//            )


//            locationDataSource.setLocationDetailEncrypted(
//                userUID = userUID,
//                locationDetail =newLocationEncrypted
//            )
        }
    }

    /**
     * @param lat1 first location latitude
     * @param lon1 first location longitude
     * @param lat2 second location latitude
     * @param lon2 second location longitude
     *
     * @return string of the distance between the two locations
     * **/


    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
        val earthRadius = 6371 // Radius of the earth in kilometers

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = (earthRadius * c)
        val distanceInMetres = (distance * 1000).toInt()

        return when{
            distanceInMetres <= 1000 -> {
                "$distanceInMetres m"
            }

            distanceInMetres > 1000000 -> {
                "> 1k km"
            }

            else -> {
                "${distanceInMetres/1000} km"
            }
        }

    }




    /**
     * get location from friends
     * @param friendsLIst friend uid list
     */
    fun getFriendsLocationDetails(friendsLIst: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUserLocation = locationDataSource.fetchCurrentLocation().first()



                val friendsLocationList = mutableListOf<FriendLocation>()

                friendsLIst.forEach { friendUID ->
                    val locationDetail = locationDataSource.getLocationByUserUID(userUID = friendUID)
//                    val locationDetail = locationDataSource.getEncryptedLocationByUser(userUID = friendUID)

//                    lateinit var locationInfo: LocationDetail

//                    val locationInfo = LocationDetail(
//                        locationDetail = LocationModel(
//                            latitude = cryptoManager.decryptDouble(locationDetail.locationDetail!!.latitude, locationDetail.locationDetail.latitudeIv),
//                            longitude = cryptoManager.decryptDouble(locationDetail.locationDetail.longitude, locationDetail.locationDetail.longitudeIv)
//                        )
//                    )
//                     locationDataSource.getEncryptedLocationOfUserUIDFlow(userUID = friendUID).collect { encryptedLocation ->
//
//                         Log.d("encrypted location flow", encryptedLocation.locationDetail!!.latitude)
//                        locationInfo = LocationDetail(
////                            lat = cryptoManager.decryptDouble(encryptedLocation.locationDetail.latitude!!.first, encryptedLocation.locationDetail.latitude.second),
////                            long = cryptoManager.decryptDouble(encryptedLocation.locationDetail.longitude!!.first, encryptedLocation.locationDetail.longitude.second)
//                            locationDetail = LocationModel(
//                                latitude = cryptoManager.decryptDouble(encryptedLocation.locationDetail!!.latitude, encryptedLocation.locationDetail.latitudeIv),
////                                latitude = cryptoManager.decryptDouble(encryptedLocation.locationDetail!!.latitude.first, encryptedLocation.locationDetail.latitude.second),
//                                longitude = cryptoManager.decryptDouble(encryptedLocation.locationDetail.longitude, encryptedLocation.locationDetail.longitudeIv)
////                                longitude = cryptoManager.decryptDouble(encryptedLocation.locationDetail.longitude.first, encryptedLocation.locationDetail.longitude.second)
//                            )
//                        )
//                    }


//                    val user = closeUserDataSource.getCloseUserByUid(closeUid = friendUID)
                    val distance = calculateDistance(
                        lat1 = currentUserLocation.latitude,
                        lon1 = currentUserLocation.longitude,
//                        lat2 = locationInfo.locationDetail.latitude,
//                        lon2 = locationInfo.locationDetail.longitude
                        lat2 = locationDetail.locationDetail.latitude,
                        lon2 = locationDetail.locationDetail.longitude
                    )


                    Log.d("Getting Location detail", "location from $locationDetail")
//                    Log.d("Getting Location detail", "location from ${locationInfo.locationDetail}")


                    friendsLocationList.add(
                        FriendLocation(
                            closerUser = closeUserDataSource.getCloseUserByUid(closeUid = friendUID),
                            locationCoordinates = locationDataSource.getLocationByUserUID(userUID = friendUID),
//                            locationCoordinates = locationInfo,
                            distanceBetweenCurrentUserLocation = distance
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