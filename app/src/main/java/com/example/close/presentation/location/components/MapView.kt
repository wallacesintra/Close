package com.example.close.presentation.location.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.close.R
import com.example.close.presentation.components.Loading
import com.example.close.presentation.location.models.FriendLocation
import com.example.close.presentation.location.models.LocationDetails
import com.example.close.presentation.location.models.SharingState
import com.example.close.utils.ConvertImgToBitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapView(
    locationDetails: LocationDetails,
    friendLocation: List<FriendLocation> = emptyList(),
    loadFriendsLocation: () -> Unit = {},
    sharingState: SharingState
) {

    LaunchedEffect(key1 = Unit) {
        loadFriendsLocation()
    }

    val context = LocalContext.current

    val currentUserLocation = LatLng(locationDetails.lat, locationDetails.long)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentUserLocation, 17f)
    }

    val markerIcon = ConvertImgToBitmapDescriptor().getBitmapDescriptorFromBitmap(context = context, R.drawable.female_dp)

    Column {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState
        ){
            Circle(
                center = currentUserLocation,
                radius = 200.00,
                strokeWidth = 1.0f,
                strokeColor = MaterialTheme.colorScheme.tertiary,
                fillColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            )
            Marker(
                state = MarkerState(position = currentUserLocation),
                title = "You",
                icon = markerIcon
            )

            when(sharingState){
                is SharingState.Error -> {}
                SharingState.Loading -> { Loading() }
                is SharingState.Success -> {
                    if (sharingState.friendsLocationList.isNotEmpty()){
                        for (location in sharingState.friendsLocationList){
                            Marker(
                                state = MarkerState(position = location.locationCoordinates!!),
                                icon = markerIcon,
                                title = location.closerUser.username,
//                    tag = "friend tag",
//                    onClick = { true},
                                onInfoWindowLongClick = { Log.i("Location onClick", "Location clicked")}
                            )
                        }
                    }
                }
            }

//            if (friendLocation.isNotEmpty()){
//                for (location in friendLocation){
//                    Marker(
//                        state = MarkerState(position = location.locationCoordinates!!),
//                        icon = markerIcon,
//                        title = location.closerUser.username,
////                    tag = "friend tag",
////                    onClick = { true},
//                        onInfoWindowLongClick = { Log.i("Location onClick", "Location clicked")}
//                    )
//                }
//            }


        }

    }
}


@Preview
@Composable
fun MapViewPreview(){
//    MapView(locationDetails = LocationDetails(
//        lat = -0.710000,
//        long = 36.294319
//    )
//    )
}