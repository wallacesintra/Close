package com.example.close.presentation.location.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.data.database.models.CloseUserData
import com.example.close.presentation.components.Loading
import com.example.close.presentation.location.models.LocationDetails
import com.example.close.presentation.location.models.SharingState
import com.example.close.presentation.models.profileImagesMap
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
    sharingState: SharingState,
    currentUser: CloseUserData
) {

    val context = LocalContext.current


    val currentUserLocation = LatLng(locationDetails.lat, locationDetails.long)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentUserLocation, 17f)
    }

    val bitImageConverter = ConvertImgToBitmapDescriptor()

    val markerIcon = bitImageConverter.getBitmapDescriptorFromBitmap(
        context = context,
        resource = profileImagesMap[currentUser.profileImg]!!.imgResId
    )

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
                fillColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
            Marker(
                state = MarkerState(position = currentUserLocation),
                title = "You",
                icon = markerIcon
            )

            when(sharingState){
                is SharingState.Error -> {}
                SharingState.Loading -> { Loading(modifier = Modifier.padding(10.dp)) }
                is SharingState.Success -> {
                    if (sharingState.friendsLocationList.isNotEmpty()){
                        for (location in sharingState.friendsLocationList){

                            val friendLocation = location.locationCoordinates?.let { LatLng(it.locationDetail.latitude, location.locationCoordinates.locationDetail.longitude) }

                            val friendIconMarker = bitImageConverter.getBitmapDescriptorFromBitmap(
                                context = context,
                                resource = profileImagesMap[location.closerUser.profileImg]!!.imgResId
                            )
                            Circle(
                                center = LatLng(
                                    friendLocation!!.latitude,
                                    friendLocation.longitude
                                ),
                                radius = 50.00,
                                strokeWidth = 1.0f,
                                fillColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                                strokeColor = MaterialTheme.colorScheme.primary
                            )

                            Marker(
                                state = MarkerState(position = friendLocation),
                                icon = friendIconMarker,
                                title = location.closerUser.username,
                                onInfoWindowLongClick = { Log.i("Location onClick", "Location clicked")}
                            )
                        }
                    }
                }
            }
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