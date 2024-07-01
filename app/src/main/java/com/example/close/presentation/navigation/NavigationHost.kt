package com.example.close.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.close.presentation.auth.screens.AuthMain
import com.example.close.presentation.auth.screens.SignIn
import com.example.close.presentation.auth.screens.SignUp
import com.example.close.presentation.auth.viewmodel.AuthViewModel
import com.example.close.presentation.auth.viewmodel.SignInSignUpViewModel
import com.example.close.presentation.friends.screens.FriendProfileScreen
import com.example.close.presentation.friends.screens.FriendRequestsScreen
import com.example.close.presentation.friends.screens.FriendsScreen
import com.example.close.presentation.friends.screens.SearchUser
import com.example.close.presentation.friends.viewmodels.FriendRequestsViewModel
import com.example.close.presentation.location.screens.CurrentLocation
import com.example.close.presentation.location.viewmodel.LocationViewModel
import com.example.close.presentation.messaging.screens.MessageScreen
import com.example.close.presentation.profile.screens.EditProfileScreen
import com.example.close.presentation.profile.screens.ProfileScreen
import com.example.close.presentation.profile.viewmodels.CurrentUserProfileDetailsViewModel
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

    //Current user Profile details ViewModel
    val currentUserProfileDetailsViewModel: CurrentUserProfileDetailsViewModel = viewModel(factory = CurrentUserProfileDetailsViewModel.Factory)

    //LocationViewModel
    val locationViewModel: LocationViewModel= viewModel(factory = LocationViewModel.factory)
    val currentLocation = locationViewModel.location.collectAsState().value
    val locationDetails= locationViewModel.locationState

    //search user viewmodel
    val searchUserViewModel: SearchUserViewModel = viewModel(factory = SearchUserViewModel.Factory)

    //friend request viewmodel
    val friendRequestsViewModel: FriendRequestsViewModel = viewModel(factory = FriendRequestsViewModel.Factory)

    Scaffold(

        bottomBar = {
            if (auth.currentUser != null){
                BottomAppBar(
//                containerColor = Color.Transparent
                ) {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Screens.forEach {screen ->
                            IconButton(
                                colors = IconButtonColors(
                                    containerColor = if (currentDestination?.hierarchy?.any { it.route ==  screen.route} == true)
                                        MaterialTheme.colorScheme.primary else Color.Transparent,
                                    contentColor = if (currentDestination?.hierarchy?.any { it.route ==  screen.route} == true)
                                        MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    navController.navigate(screen.route){
                                        popUpTo(navController.graph.findStartDestination().id){
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }}) {
                                Icon(painter = painterResource(id = screen.icon!!), contentDescription = "navigate to ${screen.route}")
                            }
                        }
                    }
                }
            }

        }
    ) {paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (auth.currentUser != null) Screen.Location.route else Screen.AuthMain.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.AuthMain.route){
                LaunchedEffect(key1 = Unit) {
                    if (auth.currentUser != null){
                        navController.navigate(Screen.Location.route)
                    }
                }

                AuthMain(
                    signInEvent = { navController.navigate(Screen.SignIn.route) },
                    signUpEvent = { navController.navigate(Screen.SignUp.route) }
                )
            }
            composable(Screen.SignIn.route){
                SignIn(
                    authViewModel = authViewmodel,
                    signInSignUpViewModel = signInSignUpViewModel,
                    goToSignUp = {navController.navigate(Screen.SignUp.route)},
                    goToProfile = {navController.navigate(Screen.Profile.route)},
                    goBackEvent = { navController.popBackStack() }
                )}
            composable(Screen.SignUp.route){
                SignUp(
                    signInSignUpViewModel = signInSignUpViewModel,
                    authViewModel = authViewmodel,
                    goToSignIn = {navController.navigate(Screen.SignIn.route)},
                    goToProfile = {navController.navigate(Screen.Profile.route)},
                    goBackEvent = { navController.popBackStack() }
                ) }
            composable(Screen.Profile.route){
                ProfileScreen(
                    userData = currentUser,
                    signOutEvent = { authViewmodel.signOutUser() },
                    goEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    goAppSetting = {},
                    inviteFriendEvent = {}
                )
            }

            composable(Screen.EditProfile.route){
                EditProfileScreen(
                    userData = currentUser,
                    currentUserProfileDetailsViewModel = currentUserProfileDetailsViewModel
                )
            }

            composable(Screen.SearchUser.route){
                SearchUser(
                    searchUserViewModel = searchUserViewModel,
                    currentUserUid = currentUser.uid,
                    goToFriendProfile = {userUid ->
                        navController.navigate(Screen.FriendProfile.createRoute(userUid))
                    }
                )
            }

            composable(Screen.Location.route){
                CurrentLocation(locationState = locationDetails)
            }

            composable(
                route = Screen.FriendProfile.route,
                arguments = Screen.FriendProfile.navArguments
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

            composable(Screen.FriendRequest.route){
                FriendRequestsScreen(currentUserUid = currentUser.uid, friendRequestsViewModel = friendRequestsViewModel)
            }

            composable(Screen.Messages.route){
                MessageScreen()
            }

            composable(Screen.Friends.route){
                FriendsScreen(
                    friendsList = currentUser.friends,
                    currentUserProfileDetailsViewModel = currentUserProfileDetailsViewModel,
                    goToFriendRequest = {navController.navigate(Screen.FriendRequest.route)}
                )
            }
        }
    }
}