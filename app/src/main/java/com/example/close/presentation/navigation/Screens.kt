package com.example.close.presentation.navigation

import androidx.navigation.NamedNavArgument

sealed class Screens(val route: String, val navArguments: List<NamedNavArgument> = emptyList()){
    data object AuthMain: Screens("AuthMain")

    data object SignIn: Screens("SignIn")

    data object SignUp: Screens("SignUp")

    data object Profile: Screens("Profile")

    data object EditProfile: Screens("EditProfile")

    data object Location: Screens("location")

}