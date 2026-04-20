package com.project.momentum.navigation.viewmodel

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
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import com.project.momentum.features.settings.repo.ServerSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            AuthResult.Error(AuthError.NoInternetConnectionError)
        }


}

sealed interface AppStartState {
    data object Loading : AppStartState
    data object NoInternetConnection : AppStartState
    data object Authorized : AppStartState
    data object Unauthorized : AppStartState
}


@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val auth: AuthUseCase,
    private val serverRep: ServerSettingsRepository
) : ViewModel() {
    var state by mutableStateOf<AppStartState>(AppStartState.Loading)
        private set

    private val _serverSettings = MutableStateFlow<ServerSettingsStateDTO?>(null)
    val serverSettings = _serverSettings.asStateFlow()

    fun loadServerSettings() {
        viewModelScope.launch {
            serverRep.getServerSettingsInfo()
                .onSuccess { _serverSettings.value = it }
                .onFailure { /* обработка */ }
        }
    }

    fun updateServerSettings(newState: ServerSettingsStateDTO) {
        _serverSettings.value = newState
    }

    fun getServerSettings() : ServerSettingsStateDTO?{
        return _serverSettings.value
    }

    fun restoreSession() {
        if (state != AppStartState.Loading) return

        viewModelScope.launch {
            val res = auth.authorize()


            state = when (res) {
                is AuthResult.Success -> {
                    if (res.token != null) {
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

            loadServerSettings()
        }
    }

    fun retrySession() {
        state = AppStartState.Loading
        restoreSession()
    }

}
