package com.example.close.presentation.auth

import androidx.navigation.NamedNavArgument

sealed class AuthScreens(val route: String, val navArguments: List<NamedNavArgument> = emptyList()){
    data object AuthMain: AuthScreens("AuthMain")

    data object SignIn: AuthScreens("SignIn")

    data object SignUp: AuthScreens("SignUp")

}