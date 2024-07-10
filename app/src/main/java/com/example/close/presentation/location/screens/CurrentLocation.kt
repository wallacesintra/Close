package com.example.close.presentation.location.screens

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.close.data.location.model.LocationModel
import com.example.close.presentation.components.Loading
import com.example.close.presentation.location.components.MapView
import com.example.close.presentation.location.models.LocationState
import com.example.close.presentation.location.viewmodel.LocationViewModel


@Composable
fun CurrentLocation(
    currentUserUID: String,
    friendsList: List<String>,
    locationViewModel: LocationViewModel
){
    val locationState = locationViewModel.locationState

    val sharingState = locationViewModel.sharingState

    LaunchedEffect(key1 = Unit) {
        locationViewModel.getCurrentLocation()
        Log.w("user uid compose",currentUserUID)
        locationViewModel.receiveLocationsFromFriends(userUID = currentUserUID)
    }



    when(locationState){
        is LocationState.Error -> Text(text = "error: ")
        LocationState.Loading -> { Loading() }

        is LocationState.Success -> {
            val location = locationState.locationDetails

//            val currentLocation = LatLng(location.lat, location.long)
            val currentLocation = LocationModel(
                latitude = locationState.locationDetails.lat,
                longitude = locationState.locationDetails.long
            )


//            locationViewModel.shareLocationToFriends(userUID = currentUserUID , friendsLIst = friendsList, locationDetail = currentLocation)

            locationViewModel.shareLocationToFriends(
                userUID = currentUserUID,
                friendsLIst = friendsList,
//                friendsLIst = emptyList(),
                locationDetail = currentLocation
            )

            MapView(
                locationDetails = location,
                sharingState = sharingState
//                friendLocation = sharingState.friendsLocationList

//                when(sharingState){
//                    is SharingState.Error -> { Text(text = "error : " + sharingState.error)}
//                    SharingState.Loading -> { Loading() }
//                    is SharingState.Success -> {
//                    }
//                }
            )




        }

    }





}