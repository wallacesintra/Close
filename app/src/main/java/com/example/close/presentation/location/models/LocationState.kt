package com.example.close.presentation.location.models

import com.example.close.presentation.location.LocationDetails

sealed interface LocationState {

    data class Success(val locationDetails: LocationDetails): LocationState

    object Error: LocationState

    object Loading: LocationState
}