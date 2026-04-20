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
    //val serverSettingsState: ServerSettingsStateDTO = ServerSettingsStateDTO(),
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

    private val _state = MutableStateFlow<SettingsState?>(null)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>(replay = 0)
    val effects = _effects.asSharedFlow()


    init {
        viewModelScope.launch {
            try {
                getLocalSettingsInfo()
            } catch (e: Exception) {
                _effects.emit(SettingsEffect.ShowError("Ошибка инициализации"))
            }
        }
    }


    fun onEvent(event: SettingsEvent, appStartViewModel: AppStartViewModel) {
        val server = appStartViewModel.getServerSettings() ?: return

        when (event) {
            SettingsEvent.OnInAppNotifications ->
                onSettingChange(appStartViewModel) {
                    serverRep.changeInAppNotifications(!server.inAppNotifications)
                }

            SettingsEvent.OnPublicationsEnabled ->
                onSettingChange(appStartViewModel) {
                    serverRep.changePublicationsEnabled(!server.publicationsEnabled)
                }

            SettingsEvent.OnReactionsEnabled ->
                onSettingChange(appStartViewModel) {
                    serverRep.changeReactionsEnabled(!server.reactionsEnabled)
                }

            SettingsEvent.OnRecommendToContacts ->
                onSettingChange(appStartViewModel) {
                    serverRep.changeRecommendToContacts(!server.recommendToContacts)
                }

            SettingsEvent.OnAllowAddFromAnyone ->
                onSettingChange(appStartViewModel) {
                    serverRep.changeAllowAddFromAnyone(!server.allowAddFromAnyone)
                }

            SettingsEvent.OnConfirmBeforePosting ->
                onConfirmBeforePosting()
        }
    }

    fun bind(appStartViewModel: AppStartViewModel) {
        viewModelScope.launch {
            appSettings.confirmBeforePost.collect { local ->
                _state.value = SettingsState(
                    localSettingsState = LocalSettingsStateDTO(local),
                    isLoading = false
                )
            }
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