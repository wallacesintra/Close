package com.example.close.presentation.auth.viewmodel

import android.util.Log
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
import com.example.close.data.database.CloseUserDataSource
import com.example.close.presentation.auth.models.AuthState
import com.example.close.presentation.models.CloseUserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDataSource: UserDataSource,
    private val closeUserDataSource: CloseUserDataSource
): ViewModel() {

    var authState: AuthState by mutableStateOf(AuthState())

    var userData by mutableStateOf(CloseUserData())


    fun createNewAccountWithEmailAndPassword(username: String, email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            val userDetails =userDataSource.createNewAccountWithEmailAndPassword(
                email = email,
                username = username,
                password = password
            )


            when (userDetails){
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    val user= CloseUserData(
                        uid = userDetails.data!!.uid,
                        username = username,
                        email = userDetails.data.email
                    )

                    closeUserDataSource.addNewCloseUser(newUser = user)

                    userData = userData.copy(
                        uid = user.uid,
                        username = user.username,
                        email = user.email
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

                    val signedInUser =closeUserDataSource.getSignInUser(uid)

                    userData = userData.copy(
                        uid = signedInUser.uid,
                        username = signedInUser.username,
                        email = signedInUser.email
                    )


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
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val userDataSource = application.container.userDataSource
                val closeUserDataSource = application.container.closeUserDataSource

                AuthViewModel(userDataSource = userDataSource, closeUserDataSource = closeUserDataSource)
            }
        }
    }
}