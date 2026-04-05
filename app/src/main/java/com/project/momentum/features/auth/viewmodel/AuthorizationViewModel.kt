package com.project.momentum.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.project.momentum.features.auth.models.LoginStep
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.data.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val repository: RegistrationRepository,
) : LoginViewModel() {
    override fun nextStep() {
        validateCurrentStep()
        if (_state.value.isError) return

        when (_state.value.currentStep) {
            LoginStep.LOGIN -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    if (repository.checkAuthorizationLoginDB(_state.value)) {
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
                    val refreshToken = repository.login(_state.value)
                    if (refreshToken != null) {
                        repository.apply {
                            saveToken(refreshToken)
                            authorize() // TODO сохранять куда-нибудь
                        }
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
                                errorMessage = ErrorLogin.CodeError.INVALID
                            )
                        }
                    }
                }
            }

            LoginStep.VERIFICATION -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val token = repository.checkAuthorizationUserCode(_state.value)
                    if (token != null) {
                        repository.apply {
                            saveToken(token)
                            authorize()         // TODO сохранять куда-нибудь
                        }
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
        validateCurrentStep()
    }

    fun onCodeAuthorization() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            while (true) {
                if (repository.sendCode(_state.value)) break
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