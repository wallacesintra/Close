package com.example.close.presentation.location.models

sealed interface LocationState {

    data class Success(val locationDetails: LocationDetails): LocationState

    data object Error: LocationState

    data object Loading: LocationState

}