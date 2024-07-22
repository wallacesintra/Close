package com.example.close.presentation.location.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.close.presentation.location.models.FriendLocation
import com.example.close.presentation.models.profileImagesMap
import com.google.android.gms.maps.model.LatLng

@Composable
fun FriendsLocationListComponents(
    modifier: Modifier = Modifier,
    onFriendComponentClick: (LatLng) -> Unit,
    friendsList: List<FriendLocation>
){
    LazyRow(
        modifier = modifier
    ) {
        items(friendsList){friend ->

            val friendPresentLocation = LatLng(
                friend.locationCoordinates!!.locationDetail.latitude,
                friend.locationCoordinates.locationDetail.longitude
            )
            FriendLocationComponent(
                friendName = friend.closerUser.username,
                friendProfileImg = profileImagesMap[friend.closerUser.profileImg]!!.imgResId,
                distance = friend.distanceBetweenCurrentUserLocation,
                onFriendComponentClick = { onFriendComponentClick(friendPresentLocation) }
            )
        }
    }
}