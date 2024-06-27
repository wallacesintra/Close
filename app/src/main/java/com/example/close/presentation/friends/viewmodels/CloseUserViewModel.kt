package com.example.close.presentation.friends.viewmodels

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
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.database.models.CloseUsers
import com.example.close.presentation.friends.models.CloseUserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CloseUserViewModel(
    private val closeUserDataSource: CloseUserDataSource
): ViewModel() {

//    var closeUserDetails: CloseUsers by mutableStateOf(CloseUsers())
    var closeUserState: CloseUserState by mutableStateOf(CloseUserState.Loading)


    fun getCloseUserDetails(userUid: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {

                closeUserState = CloseUserState.Loading

                val details = closeUserDataSource.getCloseUserByUid(userUid)

                closeUserState = try {
                    CloseUserState.Success(
                        userDetails = CloseUsers(
                            uid = details.uid,
                            username = details.username,
                            bio = details.bio,
                            sharingLocation = details.sharingLocation
                        )
                    )
                }catch (e: Exception){
                    CloseUserState.Error(
                        error = e.message!!
                    )

                }


            }catch (e: Exception){
                closeUserState = CloseUserState.Error(
                    error = e.message!!
                )
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val closeUserDataSource = application.container.closeUserDataSource


                CloseUserViewModel(closeUserDataSource= closeUserDataSource)
            }
        }
    }
}