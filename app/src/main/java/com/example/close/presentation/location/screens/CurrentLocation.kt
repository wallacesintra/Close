package com.example.close.presentation.location.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.close.presentation.components.Loading
import com.example.close.presentation.location.components.MapView
import com.example.close.presentation.location.models.LocationState
import com.example.close.presentation.location.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.LatLng


@Composable
fun CurrentLocation(
    currentUserUID: String,
    friendsList: List<String>,
    locationViewModel: LocationViewModel

){
    val locationState = locationViewModel.locationState

    val sharingState = locationViewModel.sharingState

    LaunchedEffect(key1 = Unit) {
//        locationViewModel.createLocationContainer(userUID = currentUserUID)
        locationViewModel.receiveLocationsFromFriends(userUID = currentUserUID)
        locationViewModel.getCurrentLocation()
//        sharingLocationViewModel.shareLocationToFriends(userUID = , friendsLIst = , locationDetail = )
    }



    val context = LocalContext.current


    when(locationState){
        is LocationState.Error -> Text(text = "error: ")
        LocationState.Loading -> { Loading() }

        is LocationState.Success -> {
            val location = locationState.locationDetails

            val currentLocation = LatLng(location.lat, location.long)

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