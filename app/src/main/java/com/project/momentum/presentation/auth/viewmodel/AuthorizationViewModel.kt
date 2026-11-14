package com.project.momentum.presentation.auth.viewmodel

import com.project.momentum.presentation.auth.models.toCredentials
import com.project.momentum.domain.auth.usecase.CompleteSignInUseCase
import androidx.lifecycle.viewModelScope
import com.project.momentum.domain.auth.repository.RegistrationRepository
import com.project.momentum.presentation.auth.models.LoginStep
import com.project.momentum.presentation.auth.models.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val repository: RegistrationRepository,
    private val completeSignIn: CompleteSignInUseCase,
) : LoginViewModel() {
    override fun isValidPassword(): ErrorLogin {
        if (super.isValidPassword() !is ErrorLogin.None) return ErrorLogin.PasswordError.INVALID
        return ErrorLogin.None
    }

    override fun nextStep() {
        validateCurrentStep(isValidPassword())
        if (_state.value.isError) return

        when (_state.value.currentStep) {
            LoginStep.LOGIN -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    if (repository.checkAuthorizationLoginDB(_state.value.toCredentials())) {
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
                                errorMessage = ErrorLogin.LoginError.NOT_EXISTS_IN_DB
                            )
                        }
                    }
                }
            }

            LoginStep.PASSWORD -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val refreshToken = repository.login(_state.value.toCredentials())
                    if (refreshToken != null) {
                        completeSignIn(refreshToken)
                        _state.update {
                            it.copy(
                                currentStep = LoginStep.COMPLETED
                            )
                        }
                        _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                    } else {
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

            LoginStep.VERIFICATION -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val token = repository.checkAuthorizationUserCode(_state.value.toCredentials())
                    if (token != null) {
                        completeSignIn(token)
                        _state.update {
                            it.copy(
                                currentStep = LoginStep.COMPLETED,
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

            else -> {}
        }
    }

    override fun previousStep() {
        if (!_state.value.canGoBack) return

        _state.update {
            it.copy(
                currentStep = when (it.currentStep) {
                    LoginStep.PASSWORD -> LoginStep.LOGIN
                    LoginStep.VERIFICATION -> LoginStep.PASSWORD_RECOVERY
                    LoginStep.PASSWORD_RECOVERY -> LoginStep.PASSWORD
                    else -> it.currentStep
                }
            )
        }
        validateCurrentStep(isValidPassword())
    }

    fun onCodeAuthorization() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            while (true) {
                if (repository.sendCode(_state.value.toCredentials())) break
            }
            _state.update {
                it.copy(
                    currentStep = LoginStep.VERIFICATION,
                    isError = false,
                    isLoading = false
                )
            }
            _navigationEvents.emit(NavEvent.NavigateToNextSubScreen)
        }
    }
}
