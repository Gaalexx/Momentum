package com.project.momentum.features.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.auth.models.LoginStep
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.data.RegistrationRepository
import com.project.momentum.features.auth.features.EmailChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: RegistrationRepository,
    private val emailChecker: EmailChecker
) : LoginViewModel() {
    var passwordRepetition by mutableStateOf("")
        private set

    override fun isValidPassword(): ErrorLogin {
        if (_state.value.userData.password != passwordRepetition) {
            return ErrorLogin.PasswordError.NOT_MATCH
        }
        return super.isValidPassword()
    }

    fun updateUserPasswordRepetition(password: String) {
        passwordRepetition = password
    }

    override fun nextStep() {
        validateCurrentStep(isValidPassword())
        if (_state.value.isError) return

        when (_state.value.currentStep) {
            LoginStep.LOGIN -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val exists = when (_state.value.loginType) {
                        LoginType.EMAIL -> emailChecker.checkEmail(_state.value.userData.email)
                        else -> true //TODO: проверка на существование
                    }
                    if (exists) {
                        if (repository.checkRegistrationLoginDB(_state.value)) {
                            _state.update {
                                it.copy(
                                    currentStep = LoginStep.VERIFICATION,
                                    isError = false,
                                    isLoading = false
                                )
                            }
                            _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                        } else {
                            _state.update {
                                it.copy(
                                    isError = true,
                                    isLoading = false,
                                    errorMessage = ErrorLogin.LoginError.ALREADY_EXISTS_IN_DB
                                )
                            }
                        }

                    } else {
                        _state.update {
                            it.copy(
                                isError = true,
                                isLoading = false,
                                errorMessage = ErrorLogin.LoginError.NOT_EXISTS
                            )
                        }
                    }
                }
            }

            LoginStep.VERIFICATION -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    if (repository.checkRegistrationUserCode(_state.value)) {
                        _state.update {
                            it.copy(
                                currentStep = LoginStep.PASSWORD,
                                isError = false,
                                isLoading = false
                            )
                        }
                        _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                    } else {
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

            LoginStep.PASSWORD -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val refreshToken = repository.register(_state.value)
                    if (refreshToken != null) {
                        repository.apply {
                            saveToken(refreshToken)
                            authorize() // TODO сохранять куда-нибудь
                        }
                        _state.update {
                            it.copy(
                                currentStep = LoginStep.COMPLETED,
                                isLoading = false
                            )
                        }
                        _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                    }

                }
            }

            else -> {}
        }
    }

    override fun previousStep() {
//        if (!_state.value.canGoBack) return

        _state.update {
            it.copy(
                currentStep = when (it.currentStep) {
                    LoginStep.PASSWORD -> LoginStep.LOGIN
                    LoginStep.VERIFICATION -> LoginStep.LOGIN
                    else -> it.currentStep
                }
            )
        }
        validateCurrentStep(isValidPassword())
    }

    fun sendCodeAgain() {
        viewModelScope.launch {
            while (true) {
                if (repository.sendCode(_state.value)) break
            }
        }
    }
}