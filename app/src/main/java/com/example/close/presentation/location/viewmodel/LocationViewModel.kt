package com.example.close.presentation.location.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.location.LocationDataSource
import com.example.close.data.location.model.LocationModel
import com.example.close.presentation.location.models.LocationDetails
import com.example.close.presentation.location.models.LocationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LocationViewModel(
    private val locationDataSource: LocationDataSource
): ViewModel() {


//    var locationState: LocationState by mutableStateOf(LocationState.Loading)
    var locationState: LocationState = LocationState.Loading

    private val _location = MutableStateFlow<LocationModel?>(null)
    val location: StateFlow<LocationModel?> get() = _location


    init {
        getCurrentLocation()
    }

    private fun getCurrentLocation(){
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
                    LocationState.Error
                }

            }catch (e: Exception){
                Log.w("location fetched:failure", "failure " + e.message)

                LocationState.Error
            }

        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val locationDataSource = application.container.locationDataSource

                LocationViewModel(locationDataSource = locationDataSource)
            }
        }
    }
}