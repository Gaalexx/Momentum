package com.project.momentum.features.settings.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.auth.models.LoginStep
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.settings.models.DeleteAccountState
import com.project.momentum.features.settings.models.DeleteAccountStep
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.viewmodel.ErrorLogin
import com.project.momentum.features.settings.models.SettingsError
import com.project.momentum.features.settings.repo.DeleteAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DeleteEvent {
    data object nextStep : DeleteEvent
    data object previousStep : DeleteEvent
    data object onConfirmDelete : DeleteEvent
    data object onCancelDelete : DeleteEvent
    data object sendCodeAgain : DeleteEvent
    data class updatePasswordFstTextField(val password: String) : DeleteEvent
    data class updatePasswordScdTextField(val password: String) : DeleteEvent
    data class updateUserCode(val code: String) : DeleteEvent
}

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val repository: DeleteAccountRepository
) : ViewModel() {
    private var _state = MutableStateFlow(DeleteAccountState())
    val state: StateFlow<DeleteAccountState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>(replay = 0)
    val effects = _effects.asSharedFlow()
    private val _navigationEvents = MutableSharedFlow<NavEvent>()
    val navigationEvents: SharedFlow<NavEvent> = _navigationEvents.asSharedFlow()

    var passwordRepetition by mutableStateOf("")
        private set

    fun onEvent(event: DeleteEvent) {
        when (event) {
            is DeleteEvent.nextStep -> nextStep()
            is DeleteEvent.previousStep -> previousStep()
            is DeleteEvent.onConfirmDelete -> onConfirmDelete()
            is DeleteEvent.onCancelDelete -> onCancelDelete()
            is DeleteEvent.updatePasswordFstTextField -> {updateUserPassword(event.password)}
            is DeleteEvent.updatePasswordScdTextField -> {updateUserPasswordRepetition(event.password)}
            is DeleteEvent.updateUserCode -> {updateUserCode(event.code)}
            is DeleteEvent.sendCodeAgain -> sendCodeAgain()
        }
    }

    private fun isValidPassword(): ErrorLogin {
        if (_state.value.userData.password != passwordRepetition) {
            return ErrorLogin.PasswordError.NOT_MATCH
        }
        return ErrorLogin.None
    }

    fun isValidEmail(): ErrorLogin =
        if (Patterns.EMAIL_ADDRESS.matcher(_state.value.userData.email).matches())
            ErrorLogin.None
        else ErrorLogin.LoginError.INVALID_FORMAT

    fun isValidCode(): ErrorLogin {
        return ErrorLogin.None
    }

    fun validateCurrentStep() {
        val isValid: ErrorLogin = when (_state.value.currentStep) {
            DeleteAccountStep.PASSWORD -> isValidPassword()
            DeleteAccountStep.PASSWORD_RECOVERY -> isValidEmail()
            DeleteAccountStep.VERIFICATION -> isValidCode()
            else -> ErrorLogin.None
        }

        _state.update {
            it.copy(
                isError = isValid !is ErrorLogin.None,
                errorMessage = isValid,
                canGoNext = isValid !is ErrorLogin.None && !it.isLoading,
                canGoBack = true
            )
        }
    }

    fun updateUserPasswordRepetition(password: String) {
        passwordRepetition = password
    }

    private fun updateUserPassword(password: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(password = password)
            )
        }
    }
    fun updateUserCode(code: String) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(verificationCode = code)
            )
        }
    }

    private fun nextStep() {
        validateCurrentStep()
        if (_state.value.isError && _state.value.errorMessage != ErrorLogin.None) return

        when (_state.value.currentStep) {
            DeleteAccountStep.PASSWORD -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    repository.checkPassword(_state.value)
                        .onSuccess { success ->
                            _state.update {
                                it.copy(
                                    currentStep = DeleteAccountStep.VERIFICATION
                                )
                            }
                            _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                        }
                        .onFailure { e ->
                            _state.update {
                                it.copy(
                                    isError = true,
                                    isLoading = false,
                                    errorMessage = ErrorLogin.PasswordError.INVALID
                                )
                            }
                        }
                }
            }

            DeleteAccountStep.VERIFICATION -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    while (true) {
                        if (repository.sendCode()) break
                    }
                    repository.checkUserCode(_state.value.userData.verificationCode)
                        .onSuccess { success ->
                            _state.update {
                                it.copy(
                                    currentStep = DeleteAccountStep.DELETE_ACCOUNT,
                                    isError = false,
                                    isLoading = false
                                )
                            }
                            _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                        }
                        .onFailure { e ->
                            _state.update {
                                it.copy(
                                    isError = true,
                                    isLoading = false,
                                    errorMessage = ErrorLogin.CodeError.INVALID
                                )
                            }
                        }
                }
            }

            DeleteAccountStep.DELETE_ACCOUNT_CONFIRMATION -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            currentStep = DeleteAccountStep.DELETE_ACCOUNT,
                            showConfirmationDialog = true,
                            isError = false,
                            isLoading = false
                        )
                    }
                }
            }

            else -> {}
        }
    }

    private fun previousStep() {
        if (!_state.value.canGoBack) return

        _state.update {
            it.copy(
                currentStep = when (it.currentStep) {
                    DeleteAccountStep.VERIFICATION -> DeleteAccountStep.PASSWORD
                    else -> it.currentStep
                }
            )
        }
    }
    
    private fun onConfirmDelete() {
        _state.update { it.copy(isLoading = true, showConfirmationDialog = false) }

        viewModelScope.launch {
            repository.deleteAccount(_state.value)
                .onSuccess { success ->
                    _state.update {
                        it.copy(
                            currentStep = DeleteAccountStep.COMPLETED,
                            isError = false,
                            isLoading = false
                        )
                    }
                    _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isError = true,
                            isLoading = false,
                            errorMessage = ErrorLogin.DeleteError.NOT_EXISTS
                        )
                    }
                }
        }
    }

    private fun onCancelDelete() {
        _state.update { it.copy(showConfirmationDialog = false) }
    }

    fun sendCodeAgain() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            while (true) {
                if (repository.sendCode()) break
            }
            _state.update {
                it.copy(
                    isError = false,
                    isLoading = false
                )
            }
            //_navigationEvents.emit(NavEvent.NavigateToNextSubScreen)
        }
    }
}