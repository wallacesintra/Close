package com.example.close.presentation.location.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.close.presentation.location.components.MapView
import com.example.close.presentation.location.models.LocationState
import com.google.android.gms.maps.model.LatLng


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
            
            MapView(
                locationDetails = location,
                friendLocation = listOf(LatLng(-0.710000, 36.294319), LatLng(-1.292066, 36.821945), LatLng(-0.091702, 34.767956))
            )
            
        }
    }





}