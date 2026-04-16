package com.project.momentum.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.settings.models.SettingsError
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import com.project.momentum.features.settings.models.dto.LocalSettingsStateDTO
import com.project.momentum.features.settings.repo.AppSettingsHolder
import com.project.momentum.features.settings.repo.ServerSettingsRepository
import com.project.momentum.features.settings.repo.SettingsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState (
    val serverSettingsState: ServerSettingsStateDTO = ServerSettingsStateDTO(),
    val localSettingsState: LocalSettingsStateDTO = LocalSettingsStateDTO(),
    val isLoading: Boolean = false,
    /*val isError: Boolean = false,
    val error: SettingsError = SettingsError.Unknown(),
    val errorMessage: String? = null*/
)

sealed class SettingsEffect {
    data class ShowError(val message: String) : SettingsEffect()
}


sealed interface SettingsEvent {
    data object OnInAppNotifications : SettingsEvent
    data object OnPublicationsEnabled : SettingsEvent
    data object OnReactionsEnabled : SettingsEvent
    data object OnRecommendToContacts : SettingsEvent
    data object OnAllowAddFromAnyone : SettingsEvent
    data object OnConfirmBeforePosting : SettingsEvent
}

@HiltViewModel
class SettingsMainScreenViewModel @Inject constructor(
    private val serverRep: ServerSettingsRepository,
    private val localRep: SettingsLocalRepository,
    private val appSettings: AppSettingsHolder
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState?>(SettingsState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>(replay = 0)
    val effects = _effects.asSharedFlow()


    init {
        viewModelScope.launch {
            try {
                getLocalSettingsInfo()
                getServerSettingsInfo()
            } catch (e: Exception) {
                _effects.emit(SettingsEffect.ShowError("Ошибка инициализации"))
            }
        }
    }


    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnInAppNotifications -> onInAppNotifications()
            is SettingsEvent.OnPublicationsEnabled -> onPublicationsEnabled()
            is SettingsEvent.OnReactionsEnabled -> onReactionsEnabled()
            is SettingsEvent.OnRecommendToContacts -> onRecommendToContacts()
            is SettingsEvent.OnAllowAddFromAnyone -> onAllowAddFromAnyone()
            is SettingsEvent.OnConfirmBeforePosting -> onConfirmBeforePosting()
        }
    }

    private fun getLocalSettingsInfo() {
        _state.update { it?.copy(isLoading = true) }

        viewModelScope.launch {
            val initialLocal = appSettings.confirmBeforePost.first()

            _state.value = SettingsState(
                isLoading = false,
                localSettingsState = LocalSettingsStateDTO(
                    confirmBeforePosting = initialLocal
                )
            )
        }
    }
    private fun onConfirmBeforePosting() {
        viewModelScope.launch {
            val current = appSettings.confirmBeforePost.value
            localRep.setConfirmBeforePost(!current)
                .onSuccess { newValue ->
                    _state.update {
                        it?.copy(
                            localSettingsState = it.localSettingsState.copy(
                                confirmBeforePosting = newValue
                            )
                        )
                    }
                }
                .onFailure { e -> onError(e) }

        }
    }

    private fun onSettingChange(
        changeFun: suspend () -> Result<ServerSettingsStateDTO>
    ){
        viewModelScope.launch {
            changeFun()
                .onSuccess { newState ->
                    _state.update {
                        it?.copy(
                            serverSettingsState = newState
                        )
                    }
                }
                .onFailure { e -> onError(e) }
        }
    }
    private fun onInAppNotifications() {
        val currentState = _state.value ?: return
        val current = currentState.serverSettingsState.inAppNotifications
        onSettingChange { serverRep.changeInAppNotifications(!current) }
    }

    private fun onPublicationsEnabled() {
        val currentState = _state.value ?: return
        val current = currentState.serverSettingsState.publicationsEnabled
        onSettingChange { serverRep.changePublicationsEnabled(!current) }
    }

    private fun onReactionsEnabled() {
        val currentState = _state.value ?: return
        val current = currentState.serverSettingsState.reactionsEnabled
        onSettingChange { serverRep.changeReactionsEnabled(!current) }
    }

    private fun onRecommendToContacts() {
        val currentState = _state.value ?: return
        val current = currentState.serverSettingsState.recommendToContacts
        onSettingChange { serverRep.changeRecommendToContacts(!current) }
    }
    private fun onAllowAddFromAnyone() {
        val currentState = _state.value ?: return
        val current = currentState.serverSettingsState.allowAddFromAnyone
        onSettingChange { serverRep.changeAllowAddFromAnyone(!current) }
    }

    private fun getServerSettingsInfo() {
        _state.update { it?.copy(isLoading = true) }
        viewModelScope.launch {
            serverRep.getServerSettingsInfo()
                .onSuccess { info ->
                    _state.update {
                        it?.copy(
                            isLoading = false,
                            serverSettingsState = info
                        )
                    }
                }
                .onFailure {
                    //getErrorHandler()
                }
        }
    }


    private suspend fun onError(error: Throwable) {
        val message = when (error) {
            is SettingsError.Network -> "Нет соединения"
            is SettingsError.Unauthorized -> "Авторизация истекла"
            is SettingsError.Server -> error.serverMessage ?: "Ошибка сервера"
            else -> "Неизвестная ошибка: $error"
        }
        print("Неизвестная ошибка: $error")

        _effects.emit(SettingsEffect.ShowError(message))
    }


    /*private fun getErrorHandler(){
        _state.update {
            it?.copy(
                isError = true,
                isLoading = false,
                errorMessage = "Ошибка при получении свича"//TODO изменить ошибки
            )
        }
    }*/
}