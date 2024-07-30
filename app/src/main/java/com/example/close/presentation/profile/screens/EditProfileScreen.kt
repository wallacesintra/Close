package com.example.close.presentation.profile.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.data.users.models.CloseUserData
import com.example.close.presentation.models.profileImagesMap
import com.example.close.presentation.profile.components.EditDetail
import com.example.close.presentation.profile.components.EditProfileImg
import com.example.close.presentation.profile.viewmodels.CurrentUserProfileDetailsViewModel

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    userData: CloseUserData,
    currentUserProfileDetailsViewModel: CurrentUserProfileDetailsViewModel
){


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
    ) {

        //edit profile image
            EditProfileImg(
                currentProfileImgRes = profileImagesMap[userData.profileImg]!!.imgResId,
                changeProfileImgEvent = { newProfile ->

                    if (newProfile != userData.profileImg){
                        currentUserProfileDetailsViewModel.updateCurrentUserDetails(
                            detailToUpdate = "profileImg",
                            userUid = userData.uid,
                            newValue = newProfile
                        )
                    }

                }
            )

        //edit username
        EditDetail(
            detailToEdit = "username",
            detailValue = userData.username,
            confirmDetail = {newUsername ->
                if (newUsername != userData.username){
                    currentUserProfileDetailsViewModel.updateCurrentUserDetails(
                        detailToUpdate = "username",
                        userUid = userData.uid,
                        newValue = newUsername
                    )
                }

            },
            painter = painterResource(id = R.drawable.person)
        )

        //edit user bio
        EditDetail(
            detailToEdit = "user bio",
            detailValue = userData.bio,
            confirmDetail = { newBio ->

                if (newBio != userData.bio){
                    currentUserProfileDetailsViewModel.updateCurrentUserDetails(
                        userUid = userData.uid,
                        detailToUpdate = "bio",
                        newValue = newBio
                    )
                }



            },
            detailToEditCharacterCount = 40,
            painter = painterResource(id = R.drawable.info)
        )

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview(){
//    EditProfileScreen(
//        userData = CloseUserData(uid = "wallace", username = "wallace sinatra")
//    )
}