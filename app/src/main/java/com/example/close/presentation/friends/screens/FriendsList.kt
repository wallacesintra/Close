package com.example.close.presentation.friends.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.data.users.models.CloseUser
import com.example.close.presentation.components.LargeText
import com.example.close.presentation.components.Loading
import com.example.close.presentation.components.MediumFriendProfileContainer
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.models.profileImagesMap
import com.example.close.presentation.profile.models.DetailsState
import com.example.close.presentation.profile.viewmodels.CurrentUserProfileDetailsViewModel

@Composable
fun FriendsList(
    friendsList: List<CloseUser>,
){
    Column {

        MediumText(
            text = if (friendsList.size > 1 || friendsList.isEmpty()) "${friendsList.size} friends" else "${friendsList.size} friend",
            modifier = Modifier.padding(10.dp),
            isBold = true
        )

        LazyColumn {
            items(friendsList){friend ->
                MediumFriendProfileContainer(userUid = friend.uid, username = friend.username, imgResId = profileImagesMap[friend.profileImg]!!.imgResId)
            }
        }
    }
}

@Composable
fun FriendsScreen(
    friendsList: List<String>,
    currentUserProfileDetailsViewModel: CurrentUserProfileDetailsViewModel,
    goToFriendRequest: () -> Unit
){
    val detailsState = currentUserProfileDetailsViewModel.detailsState

    LaunchedEffect(key1 = Unit) {
        currentUserProfileDetailsViewModel.loadFriendsList(friendsList)
    }

    Column(
        modifier = Modifier.padding(10.dp)
    ){
        LargeText(text = stringResource(id = R.string.friends), isBold = false, modifier = Modifier.padding(vertical = 10.dp))

        MediumText(
            text = stringResource(id = R.string.friend_requests),
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(10.dp)
                .clickable(
                    onClick = goToFriendRequest
                )
        )



        when(detailsState){
            is DetailsState.Error -> { Text(text = "error....")
            }
            DetailsState.Loading -> { Loading(modifier = Modifier.padding(10.dp)) }
            is DetailsState.Success -> {
                FriendsList(friendsList = detailsState.friendsList)
            }
        }
    }



}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FriendsScreenPreview(){
    FriendsList(friendsList = listOf(CloseUser(uid = "sysfiiyg", username = "wallace",)))
//    FriendsScreen(friendsList = emptyList())
}