package com.example.close.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screens(val route: String, val navArguments: List<NamedNavArgument> = emptyList()){
    data object AuthMain: Screens("AuthMain")

    data object SignIn: Screens("SignIn")

    data object SignUp: Screens("SignUp")

    data object Profile: Screens("Profile")

    data object EditProfile: Screens("EditProfile")

    data object Location: Screens("location")

    data object SearchUser: Screens("SearchUsers")

    data object FriendRequest: Screens("FriendRequest")

    data object FriendProfile: Screens(
        route = "FriendProfileScreen/{userUid}",
        navArguments = listOf(navArgument("userUid"){
            type = NavType.StringType
        })
    ){
        fun createRoute(userUid: String) = "FriendProfileScreen/${userUid}"
    }

}