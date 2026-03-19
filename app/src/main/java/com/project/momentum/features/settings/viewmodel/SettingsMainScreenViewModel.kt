package com.project.momentum.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.settings.models.SettingsState
import com.project.momentum.features.settings.models.dto.SwitchStateDTO
import com.project.momentum.features.settings.repo.SettingsMainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsMainScreenViewModel @Inject constructor(
    private val repository: SettingsMainRepository
) : ViewModel() {
    private var _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()
    /*private val _navigationEvents = MutableSharedFlow<Boolean>()
    val navigationEvents: SharedFlow<Boolean> = _navigationEvents.asSharedFlow()

    private val _publicationsEnabled = MutableSharedFlow<Boolean>()
    var publicationsEnabled: SharedFlow<Boolean> = _publicationsEnabled.asSharedFlow()

    private val _reactionsEnabled = MutableSharedFlow<Boolean>()
    var reactionsEnabled: SharedFlow<Boolean> = _reactionsEnabled.asSharedFlow()

    private val _inAppNotifications = MutableSharedFlow<Boolean>()
    var inAppNotifications: SharedFlow<Boolean> = _inAppNotifications.asSharedFlow()

    private val _recommendToContacts = MutableSharedFlow<Boolean>()
    var recommendToContacts: SharedFlow<Boolean> = _recommendToContacts.asSharedFlow()

    private val _allowAddFromAnyone = MutableSharedFlow<Boolean>()
    var allowAddFromAnyone: SharedFlow<Boolean> = _allowAddFromAnyone.asSharedFlow()

    private val _confirmBeforePosting = MutableSharedFlow<Boolean>()
    var confirmBeforePosting: SharedFlow<Boolean> = _confirmBeforePosting.asSharedFlow()*/

    fun onInAppNotifications() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeInAppNotifications()
            onErrorHandler(response)
        }
    }

    fun onPublicationsEnabled() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changePublicationsEnabled()
            onErrorHandler(response)
        }
    }

    fun onReactionsEnabled() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeReactionsEnabled()
            onErrorHandler(response)
        }
    }

    fun onRecommendToContacts() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeRecommendToContacts()
            onErrorHandler(response)
        }
    }

    fun onAllowAddFromAnyone() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeAllowAddFromAnyone()
            onErrorHandler(response)
        }
    }

    fun onConfirmBeforePosting() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeConfirmBeforePosting()
            onErrorHandler(response)
        }
    }

    fun getInAppNotifications() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.getInAppNotifications()
            if (response != null){
                _state.update {
                    it.copy(
                        isLoading = false,
                        inAppNotifications = response,
                    )
                }
            }
            else {
                getErrorHandler()
            }
        }
    }

    fun getPublicationsEnabled() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.getPublicationsEnabled()
            if (response != null){
                _state.update {
                    it.copy(
                        isLoading = false,
                        publicationsEnabled = response,
                    )
                }
            }
            else {
                getErrorHandler()
            }
        }
    }

    fun getReactionsEnabled() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.getReactionsEnabled()
            if (response != null){
                _state.update {
                    it.copy(
                        isLoading = false,
                        reactionsEnabled = response,
                    )
                }
            }
            else {
                getErrorHandler()
            }
        }
    }

    fun getRecommendToContacts() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.getRecommendToContacts()
            if (response != null){
                _state.update {
                    it.copy(
                        isLoading = false,
                        recommendToContacts = response,
                    )
                }
            }
            else {
                getErrorHandler()
            }
        }
    }

    fun getAllowAddFromAnyone() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.getAllowAddFromAnyone()
            if (response != null){
                _state.update {
                    it.copy(
                        isLoading = false,
                        allowAddFromAnyone = response,
                    )
                }
            }
            else {
                getErrorHandler()
            }
        }
    }

    fun getConfirmBeforePosting() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.getConfirmBeforePosting()
            if (response != null){
                _state.update {
                    it.copy(
                        isLoading = false,
                        confirmBeforePosting = response,
                    )
                }
            }
            else {
                getErrorHandler()
            }
        }
    }

    fun onErrorHandler(response: Boolean){
        if (response){
            _state.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
        else {
            _state.update {
                it.copy(
                    isError = true,
                    isLoading = false,
                    errorMessage = "Ошибка при смене состояния свича"//TODO изменить ошибки
                )
            }
        }
    }

    fun getErrorHandler(){
        _state.update {
            it.copy(
                isError = true,
                isLoading = false,
                errorMessage = "Ошибка при получении свича"//TODO изменить ошибки
            )
        }
    }
}