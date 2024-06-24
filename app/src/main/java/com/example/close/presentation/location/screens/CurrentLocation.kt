package com.example.close.presentation.location.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.close.R
import com.example.close.presentation.location.models.LocationState
import com.example.close.utils.ConvertImgToBitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun CurrentLocation(
    locationState: LocationState
){

    val context = LocalContext.current



    when(locationState){
        LocationState.Error -> Text(text = "error......")
        LocationState.Loading -> Text(text = "loading.....")
        is LocationState.Success -> {
            val location = locationState.locationDetails
            val currentUserLocation = LatLng(location.lat, location.long)

            val kasarani= LatLng(-0.710000, 36.294319)

            val nairobiCBD = LatLng(-1.292066, 36.821945)

            val kisumu = LatLng(-0.091702, 34.767956)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentUserLocation, 17f)
            }

            val markerIcon = ConvertImgToBitmapDescriptor().getBitmapDescriptorFromBitmap(context = context, R.drawable.female_dp)


            Column {
                Text(text = "Current")

//                Text(text = location.lat.toString())
//                Text(text = location.long.toString())

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
                        fillColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    )
                    Marker(
                        state = MarkerState(position = currentUserLocation),
                        title = "You",
//                        snippet = "Snippet in Singapore",
                        icon = markerIcon
                    ){

                    }

                    Marker(
                        state = MarkerState(position = nairobiCBD),
                        title = "Singapore",
                        snippet = "Snippet in Singapore",
                        icon = markerIcon

                    )
                    



                    Marker(
                        state = MarkerState(position = kasarani),
                        title = "Singapore",
                        snippet = "Snippet in Singapore",
                        icon = markerIcon
                    )


                    Marker(
                        state = MarkerState(position = kisumu),
                        title = "Singapore",
                        snippet = "Snippet in Singapore",
                        icon = markerIcon
                    )
                }

            }
        }
    }





}