package com.example.close.data.location.model

/**
 * @property userUID: unique identifier for the document
 * @property sendingList: list of the user UIDs' that user is sharing location details
 * @property receivingList: list of the user UIDs' that user is receiving their location details
 */
data class LocationSharingList(
    val userUID: String = "",
    val sendingList: List<String> = emptyList(),
    val receivingList: List<String> = emptyList()
)