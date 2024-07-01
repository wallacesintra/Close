package com.example.close.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.close.R

sealed class Screen(val route: String, val navArguments: List<NamedNavArgument> = emptyList(), @DrawableRes val  icon: Int? = null){
    data object AuthMain: Screen("AuthMain")

    data object SignIn: Screen("SignIn")

    data object SignUp: Screen("SignUp")

    data object Profile: Screen("Profile", icon = R.drawable.person)

    data object EditProfile: Screen("EditProfile")

    data object Location: Screen("location", icon = R.drawable.map)

    data object SearchUser: Screen("SearchUsers", icon = R.drawable.search_person)

    data object FriendRequest: Screen("FriendRequest", icon = R.drawable.group)

    data object FriendProfile: Screen(
        route = "FriendProfileScreen/{userUid}",
        navArguments = listOf(navArgument("userUid"){
            type = NavType.StringType
        })
    ){
        fun createRoute(userUid: String) = "FriendProfileScreen/${userUid}"
    }

    data object Friends: Screen("Friends", icon = R.drawable.group)

    data object Messages: Screen("Messages", icon = R.drawable.chat)

}


val Screens = listOf(
    Screen.Messages,
    Screen.SearchUser,
    Screen.Location,
    Screen.Friends,
    Screen.Profile,
)