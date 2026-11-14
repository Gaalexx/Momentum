package com.project.momentum.presentation.navigation.viewmodel

import android.util.Log
import com.project.momentum.domain.auth.model.LoginType
import com.project.momentum.domain.auth.usecase.AuthUseCase
import com.project.momentum.domain.auth.usecase.CompleteSignInUseCase
import com.project.momentum.domain.auth.usecase.AuthResult
import com.project.momentum.domain.auth.usecase.AuthError
import kotlinx.coroutines.CancellationException
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.project.momentum.domain.auth.repository.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.domain.settings.model.ServerSettings
import com.project.momentum.domain.settings.repository.ServerSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AppStartState {
    data object Loading : AppStartState
    data object NoInternetConnection : AppStartState
    data object Authorized : AppStartState
    data object Unauthorized : AppStartState
}

data class SwitchesState(
    val serverSettingsState: ServerSettings = ServerSettings(),
)


@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val auth: AuthUseCase,
    private val serverRep: ServerSettingsRepository,
    private val regRep: RegistrationRepository,
    private val completeSignIn: CompleteSignInUseCase
) : ViewModel() {
    var state by mutableStateOf<AppStartState>(AppStartState.Loading)
        private set


    private val _settingsState = MutableStateFlow(SwitchesState())
    val settingsState = _settingsState.asStateFlow()

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

    fun updateServerSettings(newState: ServerSettings) {
        _settingsState.update {
            it.copy(serverSettingsState = newState)
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

    fun onForeignIdAuth(type: LoginType) {
        viewModelScope.launch {
            val refreshToken = regRep.authorizeWithForeignId(type) ?: return@launch
            completeSignIn(refreshToken)
            loadServerSettings()
            state = AppStartState.Authorized
        }
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

            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (e: Exception) {
                Log.e("AppStartViewModel", "Error during logout: ${e.message}")
                state = AppStartState.Authorized
            }
        }
    }
}
