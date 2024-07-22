package com.example.close.presentation.location.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.close.data.database.models.CloseUserData
import com.example.close.presentation.location.models.FriendLocation
import com.example.close.presentation.location.models.LocationDetails
import com.example.close.presentation.models.profileImagesMap
import com.example.close.utils.ConvertImgToBitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapView(
    currentUserLocationDetails: LocationDetails,
    friendListCoordinates: List<FriendLocation>,
    currentUser: CloseUserData,
    cameraPositionState: CameraPositionState
) {

    val context = LocalContext.current


    val currentUserLocation = LatLng(currentUserLocationDetails.lat, currentUserLocationDetails.long)
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(currentUserLocation, 17f)
////        position = mapCenter
//    }

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

            friendListCoordinates.forEach { location ->

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

//            when(sharingState){
//                is SharingState.Error -> {}
//                SharingState.Loading -> { Loading(modifier = Modifier.padding(10.dp)) }
//                is SharingState.Success -> {
//                    if (sharingState.friendsLocationList.isNotEmpty()){
//                        Box {
//                            FriendsLocationListComponents(friendsList = sharingState.friendsLocationList)
//
//                            for (location in sharingState.friendsLocationList){
//
//                                val friendLocation = location.locationCoordinates?.let { LatLng(it.locationDetail.latitude, location.locationCoordinates.locationDetail.longitude) }
//
//                                val friendIconMarker = bitImageConverter.getBitmapDescriptorFromBitmap(
//                                    context = context,
//                                    resource = profileImagesMap[location.closerUser.profileImg]!!.imgResId
//                                )
//                                Circle(
//                                    center = LatLng(
//                                        friendLocation!!.latitude,
//                                        friendLocation.longitude
//                                    ),
//                                    radius = 50.00,
//                                    strokeWidth = 1.0f,
//                                    fillColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
//                                    strokeColor = MaterialTheme.colorScheme.primary
//                                )
//
//                                Marker(
//                                    state = MarkerState(position = friendLocation),
//                                    icon = friendIconMarker,
//                                    title = location.closerUser.username,
//                                    onInfoWindowLongClick = { Log.i("Location onClick", "Location clicked")}
//                                )
//
//
//                            }
//
//                        }
//
//                    }
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