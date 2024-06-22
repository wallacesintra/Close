package com.example.close.presentation.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import com.example.close.data.database.CloseUserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val closeUserDataSource: CloseUserDataSource
): ViewModel() {

    fun updateCurrentUserDetails(detailToUpdate: String, userUid: String, newValue: String){
        viewModelScope.launch(Dispatchers.IO) {

            closeUserDataSource.updateDetail(
                detailToUpdate = detailToUpdate,
                userUid = userUid,
                newValue = newValue
            )

        }

    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val closeUserDataSource = application.container.closeUserDataSource

                EditProfileViewModel(closeUserDataSource = closeUserDataSource)
            }
        }
    }

}