package com.example.close.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.close.presentation.auth.screens.AuthMain
import com.example.close.presentation.auth.screens.SignIn
import com.example.close.presentation.auth.screens.SignUp
import com.example.close.presentation.auth.viewmodel.AuthViewModel
import com.example.close.presentation.auth.viewmodel.SignInSignUpViewModel
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.location.screens.CurrentLocation
import com.example.close.presentation.location.viewmodel.LocationViewModel
import com.example.close.presentation.profile.screens.EditProfileScreen
import com.example.close.presentation.profile.screens.ProfileScreen
import com.example.close.presentation.profile.viewmodels.EditProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun NavigationHost(
    auth: FirebaseAuth
){
    val scope = CoroutineScope(Dispatchers.Main)
    val navController = rememberNavController()

    // AuthViewModel
    val authViewmodel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
    val currentUser = authViewmodel.userData
    val signInSignUpViewModel: SignInSignUpViewModel = viewModel()

    val authState = authViewmodel.authState

    //EditProfileViewModel
    val editProfileViewModel: EditProfileViewModel = viewModel(factory = EditProfileViewModel.Factory)

    //LocationViewModel
    val locationViewModel: LocationViewModel= viewModel(factory = LocationViewModel.factory)
    val currentLocation = locationViewModel.location.collectAsState().value
    val locationDetails= locationViewModel.locationState


    NavHost(
        navController = navController,
        startDestination = Screens.AuthMain.route
    ) {
        composable(Screens.AuthMain.route){
            LaunchedEffect(key1 = Unit) {
                if (auth.currentUser != null){
                    navController.navigate(Screens.Location.route)
                }
            }

            AuthMain(
                signInEvent = { navController.navigate(Screens.SignIn.route) },
                signUpEvent = { navController.navigate(Screens.SignUp.route) }
            )
        }
        composable(Screens.SignIn.route){
            SignIn(
                authViewModel = authViewmodel,
                signInSignUpViewModel = signInSignUpViewModel,
                goToSignUp = {navController.navigate(Screens.SignUp.route)},
                goToProfile = {navController.navigate(Screens.Profile.route)},
                goBackEvent = { navController.popBackStack() }
        )}
        composable(Screens.SignUp.route){
            SignUp(
                signInSignUpViewModel = signInSignUpViewModel,
                authViewModel = authViewmodel,
                goToSignIn = {navController.navigate(Screens.SignIn.route)},
                goToProfile = {navController.navigate(Screens.Profile.route)},
                goBackEvent = { navController.popBackStack() }
        ) }
        composable(Screens.Profile.route){
            ProfileScreen(
                userData = currentUser,
                signOutEvent = { authViewmodel.signOutUser() },
                goEditProfile = { navController.navigate(Screens.EditProfile.route) }
            )
        }

        composable(Screens.EditProfile.route){
            EditProfileScreen(
                userData = currentUser,
                editProfileViewModel = editProfileViewModel
            )
        }

        composable(Screens.Location.route){
            CurrentLocation(locationState = locationDetails)
        }
    }
}