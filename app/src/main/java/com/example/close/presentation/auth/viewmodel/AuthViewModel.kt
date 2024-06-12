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
import com.example.close.data.auth.UserDataSource
import com.example.close.presentation.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDataSource: UserDataSource
): ViewModel() {

//    var authState: AuthState by mutableStateOf(AuthState())
    var userData by mutableStateOf(UserData())

//    private val _userData = MutableStateFlow(UserData())

    private fun isValidEmail(email: String): Boolean{
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        return email.matches(emailRegex)
    }

    fun createNewAccountWithEmailAndPassword(username: String, email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {

            if (isValidEmail(email)){
                val userDetails =userDataSource.createNewAccountWithEmailAndPassword(
                    email = email,
                    username = username,
                    password = password
                )

                if (userDetails != null){
                    userData = userData.copy(
                        data = userDetails
                    )
                }
                
            }
        }
    }

    fun signInExistingAccountWithEmailAndPassword(email:String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            val userDetails = userDataSource.signInExistingUserWithEmailAndPassword(email = email, password = password)

            if (userDetails != null){
                userData = userData.copy(
                    data = userDetails
                )
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val userDataSource = application.container.userDataSource
                AuthViewModel(userDataSource = userDataSource)
            }
        }
    }
}