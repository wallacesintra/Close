package com.example.close.presentation.profile.viewmodels

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
import com.example.close.data.users.CloseUserDataSource
import com.example.close.presentation.profile.models.DetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CurrentUserProfileDetailsViewModel(
    private val closeUserDataSource: CloseUserDataSource
): ViewModel() {

    var detailsState: DetailsState by mutableStateOf(DetailsState.Loading)

    fun updateCurrentUserDetails(detailToUpdate: String, userUid: String, newValue: String){
        viewModelScope.launch(Dispatchers.IO) {

            closeUserDataSource.updateDetail(
                detailToUpdate = detailToUpdate,
                userUid = userUid,
                newValue = newValue
            )
        }
    }

    fun loadFriendsList(friendsList: List<String>){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                detailsState = DetailsState.Loading

                val deferred = friendsList.map { uid ->
                    async {
                        Log.d("get friends", "successful $uid")
                        closeUserDataSource.getCloseUserByUid(closeUid = uid)
                    }
                }

                val currentUserFriendsList = deferred.awaitAll()

                detailsState = DetailsState.Success(friendsList = currentUserFriendsList)
            }catch (e: Exception){
                Log.w("get friends", "failed", e)
                detailsState = DetailsState.Error(errorMessage = e.message ?: "Unknown error")
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val closeUserDataSource = application.container.closeUserDataSource

                CurrentUserProfileDetailsViewModel(closeUserDataSource = closeUserDataSource)
            }
        }
    }

}