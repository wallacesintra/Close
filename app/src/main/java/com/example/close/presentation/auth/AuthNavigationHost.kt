package com.example.close.presentation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AuthNavigationHost(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthScreens.AuthMain.route
    ) {
        composable(AuthScreens.AuthMain.route){
            AuthMain(
                signInEvent = { navController.navigate(AuthScreens.SignIn.route) },
                signUpEvent = { navController.navigate(AuthScreens.SignUp.route) }
            )
        }
        composable(AuthScreens.SignIn.route){ SignIn()}
        composable(AuthScreens.SignUp.route){ SignUp()}
    }
}