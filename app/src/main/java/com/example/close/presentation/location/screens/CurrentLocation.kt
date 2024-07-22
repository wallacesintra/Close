package com.example.close.presentation.location.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.close.data.database.models.CloseUserData
import com.example.close.data.location.model.LocationModel
import com.example.close.presentation.components.Loading
import com.example.close.presentation.location.components.FriendsLocationListComponents
import com.example.close.presentation.location.components.MapView
import com.example.close.presentation.location.components.TurnOnLocation
import com.example.close.presentation.location.models.LocationState
import com.example.close.presentation.location.models.SharingState
import com.example.close.presentation.location.viewmodel.LocationViewModel
import com.example.close.utils.LocationSetting
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState


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
                locationDetail = currentLocation
            )
            
            when(sharingState){
                is SharingState.Error -> {
                    Text(text = "error: " + sharingState.error)}
                SharingState.Loading -> { Loading(modifier = Modifier.padding(10.dp)) }
                is SharingState.Success -> {
                    Box {

                        val initialPosition = CameraPosition.fromLatLngZoom(LatLng(
                            location.lat, location.long
                        ), 17f)
                        var cameraPositionState by remember { mutableStateOf(CameraPositionState(initialPosition)) }

                        MapView(
                            currentUserLocationDetails = location,
                            friendListCoordinates = sharingState.friendsLocationList,
                            currentUser = currentUser,
                            cameraPositionState = cameraPositionState
                        )

                        FriendsLocationListComponents(
                            friendsList = sharingState.friendsLocationList,
                            onFriendComponentClick = { friendLocation ->
                                cameraPositionState = CameraPositionState(
                                    position = CameraPosition.fromLatLngZoom(
                                        friendLocation,
                                        19f
                                    )
                                )
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                    }
                }
            }






        }

    }


}