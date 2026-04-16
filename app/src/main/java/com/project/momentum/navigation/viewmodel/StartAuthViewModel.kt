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
}

sealed interface AppStartState {
    data object Loading : AppStartState
    data object NoInternetConnection : AppStartState
    data object Authorized : AppStartState
    data object Unauthorized : AppStartState
}


@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val auth: AuthUseCase
) : ViewModel() {
    var state by mutableStateOf<AppStartState>(AppStartState.Loading)
        private set


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
        }
    }

    fun retrySession() {
        state = AppStartState.Loading
        restoreSession()
    }

}
