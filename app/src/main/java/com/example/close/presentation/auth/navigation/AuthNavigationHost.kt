package com.example.close.presentation.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.close.presentation.Profile
import com.example.close.presentation.auth.screens.AuthMain
import com.example.close.presentation.auth.screens.SignIn
import com.example.close.presentation.auth.screens.SignUp
import com.example.close.presentation.auth.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthNavigationHost(
    auth: FirebaseAuth
){
    val navController = rememberNavController()

    val authViewmodel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = AuthScreens.AuthMain.route
    ) {
        composable(AuthScreens.AuthMain.route){
            LaunchedEffect(key1 = Unit) {
                if (auth.currentUser != null){
                    navController.navigate("profile")
                }
            }

            AuthMain(
                signInEvent = { navController.navigate(AuthScreens.SignIn.route) },
                signUpEvent = { navController.navigate(AuthScreens.SignUp.route) }
            )
        }
        composable(AuthScreens.SignIn.route){ SignIn(authViewModel = authViewmodel, goBackEvent = {navController.popBackStack()}) }
        composable(AuthScreens.SignUp.route){ SignUp(authViewModel = authViewmodel,goBackEvent = {navController.popBackStack()}) }
        composable("profile"){ auth.currentUser?.let { it1 -> Profile(it1) } }
    }
}