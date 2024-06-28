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
import com.example.close.presentation.friends.screens.FriendProfileScreen
import com.example.close.presentation.friends.screens.FriendRequestsScreen
import com.example.close.presentation.friends.viewmodels.FriendRequestsViewModel
import com.example.close.presentation.location.screens.CurrentLocation
import com.example.close.presentation.location.viewmodel.LocationViewModel
import com.example.close.presentation.profile.screens.EditProfileScreen
import com.example.close.presentation.profile.screens.ProfileScreen
import com.example.close.presentation.friends.screens.SearchUser
import com.example.close.presentation.profile.viewmodels.EditProfileViewModel
import com.example.close.presentation.profile.viewmodels.SearchUserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationHost(
    auth: FirebaseAuth
){
//    val scope = CoroutineScope(Dispatchers.Main)
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

    //search user viewmodel
    val searchUserViewModel: SearchUserViewModel = viewModel(factory = SearchUserViewModel.Factory)

    //friend request viewmodel
    val friendRequestsViewModel: FriendRequestsViewModel = viewModel(factory = FriendRequestsViewModel.Factory)


    NavHost(
        navController = navController,
        startDestination = Screens.AuthMain.route
    ) {
        composable(Screens.AuthMain.route){
            LaunchedEffect(key1 = Unit) {
                if (auth.currentUser != null){
                    navController.navigate(Screens.Profile.route)
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
                goEditProfile = { navController.navigate(Screens.EditProfile.route) },
                goAppSetting = {navController.navigate(Screens.FriendRequest.route)},
                inviteFriendEvent = {navController.navigate(Screens.SearchUser.route)}
            )
        }

        composable(Screens.EditProfile.route){
            EditProfileScreen(
                userData = currentUser,
                editProfileViewModel = editProfileViewModel
            )
        }

        composable(Screens.SearchUser.route){
            SearchUser(
                searchUserViewModel = searchUserViewModel,
                currentUserUid = currentUser.uid,
                goToFriendProfile = {userUid ->
                    navController.navigate(Screens.FriendProfile.createRoute(userUid))
                }
            )
        }

        composable(Screens.Location.route){
            CurrentLocation(locationState = locationDetails)
        }

        composable(

            route = Screens.FriendProfile.route,

            arguments = Screens.FriendProfile.navArguments
        ){
            val userUid = it.arguments?.getString("userUid")

            if (userUid != null) {
                FriendProfileScreen(
                    friendUid = userUid,
                    currentUserUid = currentUser.uid,
                    sendFriendRequestAction = {
                        friendRequestsViewModel.sendFriendRequest(senderUid = currentUser.uid, receiverUid = userUid)
                    }
                )
            }

        }

        composable(Screens.FriendRequest.route){
            FriendRequestsScreen(currentUserUid = currentUser.uid, friendRequestsViewModel = friendRequestsViewModel)
        }
    }
}