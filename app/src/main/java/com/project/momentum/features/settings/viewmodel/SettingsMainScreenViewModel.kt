package com.project.momentum.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.viewmodel.AccountInfoEvent
import com.project.momentum.features.account.viewmodel.AccountInfoState
import com.project.momentum.features.settings.models.dto.SettingsStateDTO
import com.project.momentum.features.settings.repo.SettingsMainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState (
    val settingsState: SettingsStateDTO = SettingsStateDTO(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)

sealed interface SettingsEvent {
    data object onInAppNotifications : SettingsEvent
    data object onPublicationsEnabled : SettingsEvent
    data object onReactionsEnabled : SettingsEvent
    data object onRecommendToContacts : SettingsEvent
    data object onAllowAddFromAnyone : SettingsEvent
    data object onConfirmBeforePosting : SettingsEvent
    data object getAccountInfo : SettingsEvent
}

@HiltViewModel
class SettingsMainScreenViewModel @Inject constructor(
    private val repository: SettingsMainRepository
) : ViewModel() {
    private var _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init{
        getSettingsInfo()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.onInAppNotifications -> onInAppNotifications()
            is SettingsEvent.onPublicationsEnabled -> onPublicationsEnabled()
            is SettingsEvent.onReactionsEnabled -> onReactionsEnabled()
            is SettingsEvent.onRecommendToContacts -> onRecommendToContacts()
            is SettingsEvent.onAllowAddFromAnyone -> onAllowAddFromAnyone()
            is SettingsEvent.onConfirmBeforePosting -> onConfirmBeforePosting()
            is SettingsEvent.getAccountInfo -> getSettingsInfo()
        }
    }

    private fun onInAppNotifications() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeInAppNotifications()
            onErrorHandler(response)
        }
    }

    private fun onPublicationsEnabled() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changePublicationsEnabled()
            onErrorHandler(response)
        }
    }

    private fun onReactionsEnabled() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeReactionsEnabled()
            onErrorHandler(response)
        }
    }

    private fun onRecommendToContacts() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeRecommendToContacts()
            onErrorHandler(response)
        }
    }

    private fun onAllowAddFromAnyone() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeAllowAddFromAnyone()
            onErrorHandler(response)
        }
    }

    private fun onConfirmBeforePosting() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = repository.changeConfirmBeforePosting()
            onErrorHandler(response)
        }
    }

    private fun getSettingsInfo() {
        viewModelScope.launch {
            val info = repository.getSettingsInfo()
            if (info != null){
                _state.update {
                    it.copy(
                        isLoading = false,
                        settingsState = info
                    )
                }
            }
            else {
                getErrorHandler()
            }
        }
    }

    private fun onErrorHandler(response: Boolean){
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

    private fun getErrorHandler(){
        _state.update {
            it.copy(
                isError = true,
                isLoading = false,
                errorMessage = "Ошибка при получении свича"//TODO изменить ошибки
            )
        }
    }
}