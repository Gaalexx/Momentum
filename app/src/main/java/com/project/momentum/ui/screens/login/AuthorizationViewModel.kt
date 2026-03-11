package com.project.momentum.ui.screens.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.project.momentum.data.LoginStep
import com.project.momentum.data.LoginType
import com.project.momentum.data.registration.NavEvent
import com.project.momentum.data.registration.RegistrationRepository
import com.project.momentum.ui.screens.registration.LoginViewModel
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
        if (!_state.value.isStepValid) return

        when (_state.value.currentStep) {
            LoginStep.LOGIN -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val exists = when (_state.value.loginType) {
                        LoginType.EMAIL -> emailChecker.canReceiveEmail(_state.value.userData.email)
                        else -> true //TODO: проверка на существование
                    }
                    if (exists) {
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
                                    //TODO: завести класс для ошибок enum или что-то поумнее
                                    errorMessage = when (_state.value.loginType) {
                                        LoginType.EMAIL -> "Аккаунта с такой почтой не существует"
                                        else -> "Аккаунта с таким телефоном не существует"
                                    }
                                )
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isError = true,
                                isLoading = false,
                                errorMessage = "Такого Email не существует"
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
                                errorMessage = "Неверный пароль"
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
                                //TODO: завести класс для ошибок enum или что-то поумнее
                                errorMessage = "Неверный код"
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
            repository.sendAuthorizationCode(_state.value)
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