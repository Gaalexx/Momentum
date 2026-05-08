package com.project.momentum.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.settings.models.SettingsError
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import com.project.momentum.features.settings.models.dto.LocalSettingsStateDTO
import com.project.momentum.features.settings.repo.AppSettingsHolder
import com.project.momentum.features.settings.repo.ServerSettingsRepository
import com.project.momentum.features.settings.repo.SettingsLocalRepository
import com.project.momentum.navigation.viewmodel.AppStartViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState (
    val isLoading: Boolean = false
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
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>(replay = 0)
    val effects = _effects.asSharedFlow()


    fun onEvent(event: SettingsEvent, appStartViewModel: AppStartViewModel) {
        val server = appStartViewModel.getSettings().serverSettingsState

        when (event) {
            SettingsEvent.OnInAppNotifications ->
                onServerSettingChange(appStartViewModel) {
                    serverRep.changeInAppNotifications(!server.inAppNotifications)
                }

            SettingsEvent.OnPublicationsEnabled ->
                onServerSettingChange(appStartViewModel) {
                    serverRep.changePublicationsEnabled(!server.publicationsEnabled)
                }

            SettingsEvent.OnReactionsEnabled ->
                onServerSettingChange(appStartViewModel) {
                    serverRep.changeReactionsEnabled(!server.reactionsEnabled)
                }

            SettingsEvent.OnRecommendToContacts ->
                onServerSettingChange(appStartViewModel) {
                    serverRep.changeRecommendToContacts(!server.recommendToContacts)
                }

            SettingsEvent.OnAllowAddFromAnyone ->
                onServerSettingChange(appStartViewModel) {
                    serverRep.changeAllowAddFromAnyone(!server.allowAddFromAnyone)
                }

            SettingsEvent.OnConfirmBeforePosting ->
                onLocalSettingChange(appStartViewModel)
        }
    }

    private fun onLocalSettingChange(appStartViewModel: AppStartViewModel) {
        viewModelScope.launch {
            val current = appStartViewModel.getSettings().localSettingsState.confirmBeforePosting
            localRep.setConfirmBeforePost(!current)
                .onSuccess { newValue ->
                    appStartViewModel.updateLocalSettings(newValue)
                }
                .onFailure { e -> onError(e) }

        }
    }

    private fun onServerSettingChange(
        appStartViewModel: AppStartViewModel,
        changeFun: suspend () -> Result<ServerSettingsStateDTO>
    ){
        viewModelScope.launch {
            changeFun()
                .onSuccess { newState ->
                    appStartViewModel.updateServerSettings(newState)
                }
                .onFailure { e -> onError(e) }
        }
    }

    private suspend fun onError(error: Throwable) {
        val message = when (error) {
            is SettingsError.Network -> "Нет соединения"
            is SettingsError.Unauthorized -> "Авторизация истекла"
            is SettingsError.Server -> error.serverMessage ?: "Ошибка сервера"
            else -> "Неизвестная ошибка: $error"
        }

        _effects.emit(SettingsEffect.ShowError(message))
    }
}