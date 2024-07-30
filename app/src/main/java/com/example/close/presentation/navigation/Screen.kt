package com.example.close.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.close.R

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList(),
    @DrawableRes val icon: Int? = null,
    val screenLabel: String? = null
){
    data object AuthMain: Screen("AuthMain")

    data object SignIn: Screen("SignIn")

    data object SignUp: Screen("SignUp")

    data object Profile: Screen("Profile", icon = R.drawable.person, screenLabel = "profile")

    data object EditProfile: Screen("EditProfile")

    data object Location: Screen("location", icon = R.drawable.map, screenLabel = "map")

    data object SearchUser: Screen("SearchUsers", icon = R.drawable.search_person, screenLabel = "search")

    data object FriendRequest: Screen("FriendRequest", icon = R.drawable.group, screenLabel = "friends requests")

    data object FriendProfile: Screen(
        route = "FriendProfileScreen/{userUid}",
        navArguments = listOf(navArgument("userUid"){
            type = NavType.StringType
        })
    ){
        fun createRoute(userUid: String) = "FriendProfileScreen/${userUid}"
    }

    data object SingleChatRoom: Screen(
        route = "SingleChatRoom/{chatRoomUID}",
        navArguments = listOf(navArgument("chatRoomUID"){
            type = NavType.StringType
        })
    ){
        fun createRoute(chatRoomUID: String) = "SingleChatRoom/${chatRoomUID}"
    }

    data object Friends: Screen("Friends", icon = R.drawable.group, screenLabel = "friends")

    data object Messages: Screen("Messages", icon = R.drawable.chat, screenLabel = "chat")

}


val Screens = listOf(
    Screen.Messages,
    Screen.SearchUser,
    Screen.Location,
    Screen.Friends,
    Screen.Profile,
)