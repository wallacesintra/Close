package com.example.close.presentation.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.auth.Resource
import com.example.close.data.auth.UserDataSource
import com.example.close.data.cometChat.CometChatAuthImp
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.database.models.CloseUserData
import com.example.close.data.location.LocationDataSource
import com.example.close.presentation.auth.models.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDataSource: UserDataSource,
    private val closeUserDataSource: CloseUserDataSource,
    private val cometChatAuthImp: CometChatAuthImp,
    private val locationDataSource: LocationDataSource
): ViewModel() {

    var authState: AuthState by mutableStateOf(AuthState())

    var userData by mutableStateOf(CloseUserData())

    init {
        getSignedUser()
    }


    private fun getSignedUser(){
        viewModelScope.launch(Dispatchers.IO) {

            when(val user =userDataSource.getSignedInUser()){
                is Resource.Error -> {}
                is Resource.Success -> {
                    val uid = user.data!!.uid

                    val currentUser= closeUserDataSource.getSignedInUser(uid)

                    userData = userData.copy(
                        uid = currentUser.uid,
                        email = currentUser.email,
                        username = currentUser.username,
                        bio = currentUser.bio,
                        profileImg = currentUser.profileImg,
                        friends = currentUser.friends,
                        shareLocation = currentUser.shareLocation
                    )
                }
            }
        }
    }


    fun createNewAccountWithEmailAndPassword(username: String, email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            val userDetails =userDataSource.createNewAccountWithEmailAndPassword(
                email = email.trim(),
                username = username.trim(),
                password = password.trim()
            )


            when (userDetails){
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    val user= CloseUserData(
                        uid = userDetails.data!!.uid,
                        username = username,
                        email = userDetails.data.email!!,
                    )

                    closeUserDataSource.addNewCloseUser(newUser = user)

                    //create comet chat user
                    cometChatAuthImp.createCometUser(userId = user.uid, username = user.username)

                    //create a locations container
                    locationDataSource.createLocationContainer(userUID = user.uid)


                    userData = userData.copy(
                        uid = user.uid,
                        username = user.username,
                        email = user.email,
                        bio = user.bio,
                        friends = user.friends,
                        shareLocation = user.shareLocation
                    )

                    authState = authState.copy(
                        isUserSignedIn = true
                    )

                }
            }

        }


    }

    fun signInExistingAccountWithEmailAndPassword(email:String, password: String){
        viewModelScope.launch(Dispatchers.IO) {

            val userDetails = userDataSource.signInExistingUserWithEmailAndPassword(email = email, password = password)

            when (userDetails){
                is Resource.Error -> {}
                is Resource.Success -> {

                    val uid = userDetails.data!!.uid

                    val signedInUser =closeUserDataSource.getSignedInUser(uid)

                    userData = userData.copy(
                        uid = signedInUser.uid,
                        username = signedInUser.username,
                        email = signedInUser.email
                    )

                    authState = authState.copy(
                        isUserSignedIn = true
                    )

                    //login to cometchat
                    cometChatAuthImp.logInUser(userId = uid)

                }
            }
        }
    }

    fun signOutUser(){
        viewModelScope.launch(Dispatchers.IO) {
            userDataSource.signOutExistingUser()

            authState = authState.copy(
                isUserSignedIn = false
            )

            userData = CloseUserData()
            cometChatAuthImp.logOutUser()
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val userDataSource = application.container.userDataSource
                val closeUserDataSource = application.container.closeUserDataSource
                val cometChatAuthImp = application.container.cometChatAuthImp
                val locationDataSource = application.container.locationDataSource

                AuthViewModel(
                    userDataSource = userDataSource,
                    closeUserDataSource = closeUserDataSource,
                    cometChatAuthImp = cometChatAuthImp,
                    locationDataSource = locationDataSource
                )
            }
        }
    }
}