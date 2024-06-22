package com.example.close.data.location

import com.example.close.data.location.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationSource {

    suspend fun fetchCurrentLocation(): Flow<LocationModel>

}