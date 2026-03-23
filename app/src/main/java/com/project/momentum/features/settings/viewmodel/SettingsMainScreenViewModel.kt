package com.project.momentum.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import com.project.momentum.features.settings.models.dto.LocalSettingsStateDTO
import com.project.momentum.features.settings.repo.ServerSettingsRepository
import com.project.momentum.features.settings.repo.SettingsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState (
    val serverSettingsState: ServerSettingsStateDTO = ServerSettingsStateDTO(),
    val localSettingsState: LocalSettingsStateDTO = LocalSettingsStateDTO(),
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
    data object getLocalSettingsInfo : SettingsEvent
}

@HiltViewModel
class SettingsMainScreenViewModel @Inject constructor(
    private val serverRep: ServerSettingsRepository,
    private val localRep: SettingsLocalRepository
) : ViewModel() {
    private var _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    val confirmBeforePostFlow: StateFlow<Boolean> =
        localRep.confirmBeforePost
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )

    init{
        getLocalSettingsInfo()
        getServerSettingsInfo()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.onInAppNotifications -> onInAppNotifications()
            is SettingsEvent.onPublicationsEnabled -> onPublicationsEnabled()
            is SettingsEvent.onReactionsEnabled -> onReactionsEnabled()
            is SettingsEvent.onRecommendToContacts -> onRecommendToContacts()
            is SettingsEvent.onAllowAddFromAnyone -> onAllowAddFromAnyone()
            is SettingsEvent.onConfirmBeforePosting -> onConfirmBeforePosting()
            is SettingsEvent.getAccountInfo -> getServerSettingsInfo()
            is SettingsEvent.getLocalSettingsInfo -> getLocalSettingsInfo()
        }
    }

    private fun onInAppNotifications() {
        _state.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            val current = _state.value.serverSettingsState.inAppNotifications
            serverRep.changeInAppNotifications(!current)
                .onSuccess { newValue ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            serverSettingsState = it.serverSettingsState.copy(
                                inAppNotifications = newValue
                            )
                        )
                    }
                }
                .onFailure { onErrorHandler() }
        }
    }

    private fun onPublicationsEnabled() {
        _state.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            val current = _state.value.serverSettingsState.publicationsEnabled
            serverRep.changePublicationsEnabled(!current)
                .onSuccess { newValue ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            serverSettingsState = it.serverSettingsState.copy(
                                publicationsEnabled = newValue
                            )
                        )
                    }
                }
                .onFailure { onErrorHandler() }
        }
    }

    private fun onReactionsEnabled() {
        _state.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            val current = _state.value.serverSettingsState.reactionsEnabled
            serverRep.changeReactionsEnabled(!current)
                .onSuccess { newValue ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            serverSettingsState = it.serverSettingsState.copy(
                                reactionsEnabled = newValue
                            )
                        )
                    }
                }
                .onFailure { onErrorHandler() }
        }
    }

    private fun onRecommendToContacts() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val current = _state.value.serverSettingsState.recommendToContacts
            serverRep.changeRecommendToContacts(!current)
                .onSuccess { newValue ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            serverSettingsState = it.serverSettingsState.copy(
                                recommendToContacts = newValue
                            )
                        )
                    }
                }
                .onFailure { onErrorHandler() }
        }
    }

    private fun onAllowAddFromAnyone() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val current = _state.value.serverSettingsState.allowAddFromAnyone
            serverRep.changeAllowAddFromAnyone(!current)
            .onSuccess { newValue ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        serverSettingsState = it.serverSettingsState.copy(
                            allowAddFromAnyone = newValue
                        )
                    )
                }
            }
            .onFailure { onErrorHandler() }
        }
    }

    private fun onConfirmBeforePosting() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val current = confirmBeforePostFlow.value
            localRep.setConfirmBeforePost(!current)
                .onSuccess { newValue ->
                    _state.update {
                        it.copy(
                            isLoading = false,
//                            localSettingsState = it.localSettingsState.copy(
//                                confirmBeforePosting = newValue
//                            )
                        )
                    }
                }
                .onFailure { onErrorHandler() }

        }
    }

    private fun getServerSettingsInfo() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            serverRep.getServerSettingsInfo()
                .onSuccess { info ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            serverSettingsState = info
                        )
                    }
                }
                .onFailure {
                    getErrorHandler()
                }
        }
    }

    private fun getLocalSettingsInfo() {
        //_state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            confirmBeforePostFlow.collect { value ->
                _state.update {
                    it.copy(
                        //isLoading = false,
                        localSettingsState = it.localSettingsState.copy(
                            confirmBeforePosting = value
                        )
                    )
                }
            }
        }
    }


    private fun onErrorHandler(){
        _state.update {
            it.copy(
                isError = true,
                isLoading = false,
                errorMessage = "Ошибка при смене состояния свича"//TODO изменить ошибки
            )
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