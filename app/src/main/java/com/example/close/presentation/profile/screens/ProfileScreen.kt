package com.example.close.presentation.profile.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.LargeActionContainer
import com.example.close.presentation.components.LargeProfileImg
import com.example.close.presentation.components.LargeText
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.models.CloseUserData

@Composable
fun ProfileScreen(
    userData: CloseUserData,
    signOutEvent: () -> Unit,
    goEditProfile: () -> Unit = {},
    goAppSetting: () -> Unit = {},
    inviteFriendEvent: () -> Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LargeProfileImg()

        LargeText(text = userData.username)

        MediumText(text = userData.bio)


        Spacer(modifier = Modifier.padding(12.dp))

        //edit current user account details
        LargeActionContainer(
            containerAction = goEditProfile,
            iconPainter = painterResource(id = R.drawable.person),
            iconDescription = stringResource(id = R.string.edit_account),
            actionText = stringResource(id = R.string.edit_account)
        )

        // customize app setting
        LargeActionContainer(
            containerAction = goAppSetting,
            iconPainter = painterResource(id = R.drawable.setting),
            iconDescription = stringResource(id = R.string.go_app_setting),
            actionText = stringResource(id = R.string.app_setting)
        )

        // invite a friend
        LargeActionContainer(
            containerAction = inviteFriendEvent,
            iconPainter = painterResource(id = R.drawable.group),
            iconDescription = stringResource(id = R.string.invite_friend),
            actionText = stringResource(id = R.string.invite_friend)
        )

        // sign out
        LargeActionContainer(
            containerAction = signOutEvent,
            iconPainter = painterResource(id = R.drawable.signout),
            iconDescription = stringResource(id = R.string.sign_out),
            actionText = stringResource(id = R.string.sign_out)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(
        userData = CloseUserData(username = "wallace sinatra" , email = "sintra@gmail.com", ),
        {}
    )
}