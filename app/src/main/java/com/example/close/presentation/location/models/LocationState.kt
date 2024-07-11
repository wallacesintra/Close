package com.example.close.presentation.location.models

sealed interface LocationState {

    data class Success(val locationDetails: LocationDetails): LocationState

    data class Error(val error: String) : LocationState

    data object Loading: LocationState

}