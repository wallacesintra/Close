package com.example.close.presentation.friends.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.close.presentation.components.LargeText
import com.example.close.presentation.components.Loading
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.components.ProfileImg
import com.example.close.presentation.friends.models.CloseUserState
import com.example.close.presentation.friends.viewmodels.CloseUserViewModel
import com.example.close.presentation.models.profileImagesMap


//@Preview(showBackground = true, showSystemUi = true,)
@Composable
fun FriendProfileScreen(
    friendUid: String,
    currentUserUid: String,
    friendsUIDList: List<String>,
    sendFriendRequestAction: () -> Unit
){
    val closeUserViewModel: CloseUserViewModel = viewModel(factory = CloseUserViewModel.Factory)

    val closeUserState = closeUserViewModel.closeUserState


    LaunchedEffect(key1 = Unit) {
        closeUserViewModel.getCloseUserDetails(userUid = friendUid)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {


        when(closeUserState){
            is CloseUserState.Error -> { Text(text = closeUserState.error)}
            CloseUserState.Loading -> { Loading()}
            is CloseUserState.Success -> {
                ProfileImg(
                    imgSize = 120.dp,
                    imageResId = profileImagesMap[closeUserState.userDetails.profileImg]!!.imgResId,
                    modifier = Modifier.padding(20.dp)
                )
                LargeText(text = closeUserState.userDetails.username)
                MediumText(text = closeUserState.userDetails.bio)

                Spacer(modifier = Modifier.padding(12.dp))

                if (closeUserState.userDetails.uid !in friendsUIDList){
                    Button(onClick = { sendFriendRequestAction() }) {
                        Text(text = "send friend request")
                    }
                }

//                if (closeUserState.userDetails.uid != currentUserUid){
//                    Button(onClick = { sendFriendRequestAction() }) {
//                        Text(text = "send friend request")
//                    }
//                }

            }
        }
    }
}