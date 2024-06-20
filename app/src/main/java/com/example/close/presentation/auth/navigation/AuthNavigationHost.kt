package com.example.close.presentation.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.close.presentation.auth.screens.Profile
import com.example.close.presentation.auth.screens.AuthMain
import com.example.close.presentation.auth.screens.SignIn
import com.example.close.presentation.auth.screens.SignUp
import com.example.close.presentation.auth.viewmodel.AuthViewModel
import com.example.close.presentation.auth.viewmodel.SignInSignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun AuthNavigationHost(
    auth: FirebaseAuth
){
    val scope = CoroutineScope(Dispatchers.Main)
    val navController = rememberNavController()

    val authViewmodel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
    val currentUser = authViewmodel.userData
    val signInSignUpViewModel: SignInSignUpViewModel = viewModel()

    val authState = authViewmodel.authState

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
        composable(AuthScreens.SignIn.route){ SignIn(
            authViewModel = authViewmodel,
            signInSignUpViewModel = signInSignUpViewModel,
            goToSignUp = {navController.navigate(AuthScreens.SignUp.route)},
            goToProfile = {navController.navigate("profile")},
            goBackEvent = { navController.popBackStack() }
        )}
        composable(AuthScreens.SignUp.route){ SignUp(
            signInSignUpViewModel = signInSignUpViewModel,
            authViewModel = authViewmodel,
            goToSignIn = {navController.navigate(AuthScreens.SignIn.route)},
            goToProfile = {navController.navigate("profile")},
            goBackEvent = { navController.popBackStack() }
        ) }
        composable("profile"){
            Profile(user = currentUser, signOut = {authViewmodel.signOutUser()} )
        }
    }
}