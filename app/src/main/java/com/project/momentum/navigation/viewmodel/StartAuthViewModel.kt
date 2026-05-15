package com.project.momentum.navigation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.project.momentum.data.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.retain.retain
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.settings.models.dto.LocalSettingsStateDTO
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import com.project.momentum.features.settings.repo.AppSettingsHolder
import com.project.momentum.features.settings.repo.ServerSettingsRepository
import com.project.momentum.features.settings.repo.SettingsLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException

sealed interface AuthError {
    data object NoInternetConnectionError : AuthError
}

sealed interface AuthResult {
    data class Success(val token: String?) : AuthResult
    data class Error(val error: AuthError) : AuthResult
}

@Singleton
class AuthUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {
    suspend fun authorize(): AuthResult =
        try {
            AuthResult.Success(registrationRepository.authorize())
        } catch (ex: Exception) {
            Log.e("Authorization", ex.message ?: "unknown")
            AuthResult.Error(AuthError.NoInternetConnectionError)
        }

    suspend fun unauthorize(): Boolean = registrationRepository.clearSession()

    suspend fun syncPushToken() {
        registrationRepository.syncPushToken()
    }
}

sealed interface AppStartState {
    data object Loading : AppStartState
    data object NoInternetConnection : AppStartState
    data object Authorized : AppStartState
    data object Unauthorized : AppStartState
}

data class SwitchesState(
    val serverSettingsState: ServerSettingsStateDTO = ServerSettingsStateDTO(),
    val localSettingsState: LocalSettingsStateDTO = LocalSettingsStateDTO(),
)


@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val auth: AuthUseCase,
    private val serverRep: ServerSettingsRepository,
    private val appSettings: AppSettingsHolder,
    private val regRep: RegistrationRepository
) : ViewModel() {
    var state by mutableStateOf<AppStartState>(AppStartState.Loading)
        private set


    private val _settingsState = MutableStateFlow(SwitchesState())
    val settingsState = _settingsState.asStateFlow()

    init {
        viewModelScope.launch {
            appSettings.confirmBeforePost.collect { local ->
                _settingsState.update {
                    it.copy(
                        localSettingsState = LocalSettingsStateDTO(
                            confirmBeforePosting = local
                        )
                    )
                }
            }
        }
    }

    fun loadServerSettings() {
        viewModelScope.launch {
            serverRep.getServerSettingsInfo()
                .onSuccess { server ->
                    _settingsState.update {
                        it.copy(serverSettingsState = server)
                    }
                }
                .onFailure {
                    // TODO: обработка ошибок
                }
        }
    }

    fun updateServerSettings(newState: ServerSettingsStateDTO) {
        _settingsState.update {
            it.copy(serverSettingsState = newState)
        }
    }

    fun updateLocalSettings(newState: LocalSettingsStateDTO) {
        _settingsState.update {
            it.copy(localSettingsState = newState)
        }
    }

    fun getSettings(): SwitchesState =
        _settingsState.value

    fun restoreSession() {
        if (state != AppStartState.Loading) return

        viewModelScope.launch {
            val res = auth.authorize()


            state = when (res) {
                is AuthResult.Success -> {
                    if (res.token != null) {
                        loadServerSettings()
                        AppStartState.Authorized
                    } else {
                        AppStartState.Unauthorized
                    }
                }

                is AuthResult.Error -> {
                    when (res.error) {
                        is AuthError.NoInternetConnectionError -> AppStartState.NoInternetConnection
                    }
                }
            }
            if (state is AppStartState.Authorized) {
                auth.syncPushToken()
            }
        }
    }

    fun endSession() {
        if (state != AppStartState.Authorized) return

        viewModelScope.launch {
            if (auth.unauthorize()) {
                state = AppStartState.Unauthorized
            } else {
                state = AppStartState.Authorized
            }
        }
    }

    fun retrySession() {
        state = AppStartState.Loading
        restoreSession()
    }

    fun onVKAuthSuccess() {
        viewModelScope.launch {
            loadServerSettings()
            auth.syncPushToken()
        }
        state = AppStartState.Authorized
    }

    fun setUnauthorized() {
        state = AppStartState.Unauthorized
    }

    fun logout() {
        viewModelScope.launch {
            try {
                regRep.clearSession()

                regRep.clearLocalAuthData()

                _settingsState.update {
                    SwitchesState()
                }

                state = AppStartState.Unauthorized

            } catch (e: Exception) {
                Log.e("AppStartViewModel", "Error during logout: ${e.message}")
                state = AppStartState.Authorized
            }
        }
    }
}
