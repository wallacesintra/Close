package com.example.close.presentation.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.close.data.database.CloseUserDataSource
import com.example.close.data.database.models.CloseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.close.CloseApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class SearchUserViewModel(
    private val closeUserDataSource: CloseUserDataSource
): ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _closeUser = MutableStateFlow<List<CloseUser>>(emptyList())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val closeUsers = searchText
        .debounce(500L)
        .onEach { _isSearching.value = true }
        .flatMapLatest { text ->
            if(text.isBlank()) {
                flowOf(_closeUser.value)
            } else {
                flow {
                    emit(closeUserDataSource.findUserByUsername(username = text.trim()))
                }
            }
        }
        .onEach { _isSearching.value = false }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _closeUser.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CloseApp)
                val closeUserDataSource = application.container.closeUserDataSource


                SearchUserViewModel(
                    closeUserDataSource = closeUserDataSource
                )
            }
        }
    }
}
