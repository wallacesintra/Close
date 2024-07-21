package com.example.close.presentation.location.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.close.data.database.models.CloseUserData
import com.example.close.data.location.model.LocationModel
import com.example.close.presentation.components.Loading
import com.example.close.presentation.location.components.MapView
import com.example.close.presentation.location.components.TurnOnLocation
import com.example.close.presentation.location.models.LocationState
import com.example.close.presentation.location.viewmodel.LocationViewModel
import com.example.close.utils.LocationSetting


@Composable
fun CurrentLocation(
    friendsList: List<String>,
    currentUser: CloseUserData,
    locationViewModel: LocationViewModel
){
    val locationState = locationViewModel.locationState

    val sharingState = locationViewModel.sharingState

    LaunchedEffect(key1 = Unit) {
        locationViewModel.getCurrentLocation()
        locationViewModel.getFriendsLocationDetails(friendsLIst = friendsList)
    }

    val context = LocalContext.current
    val isLocationEnabled = LocationSetting().isLocationEnabled(context = context)

    if (!isLocationEnabled){
        TurnOnLocation()
    }

    when(locationState){
        is LocationState.Error -> Text(text = "error: ")
        LocationState.Loading -> { Loading() }

        is LocationState.Success -> {
            val location = locationState.locationDetails
            val currentLocation = LocationModel(
                latitude = locationState.locationDetails.lat,
                longitude = locationState.locationDetails.long
            )

            locationViewModel.updateLocationDetails(
                userUID = currentUser.uid,
                friendsLIst = friendsList,
                locationDetail = currentLocation
            )

            MapView(
                locationDetails = location,
                sharingState = sharingState,
                currentUser = currentUser
            )




        }

    }


}