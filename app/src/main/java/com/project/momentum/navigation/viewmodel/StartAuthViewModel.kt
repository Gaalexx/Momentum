package com.project.momentum.navigation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.project.momentum.data.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Singleton
class AuthUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {
    suspend fun authorize(): String? {
        return registrationRepository.authorize()
    }
}

sealed interface AppStartState {
    data object Loading : AppStartState
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
            val token = auth.authorize()

            state = if (token != null) {
                AppStartState.Authorized
            } else {
                AppStartState.Unauthorized
            }
        }
    }

    suspend fun tryAuth(): Boolean {
        return auth.authorize() != null
    }
}

