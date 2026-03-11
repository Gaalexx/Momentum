package com.project.momentum.ui.screens.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.project.momentum.data.LoginType
import com.project.momentum.data.LoginStep
import com.project.momentum.data.registration.NavEvent
import com.project.momentum.data.registration.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: RegistrationRepository
) : LoginViewModel() {
    var passwordRepetition by mutableStateOf("")
        private set

    override fun isValidPassword(): PasswordState {
        if (_state.value.userData.password != passwordRepetition) {
            return PasswordState.NOT_MATCH
        }
        return super.isValidPassword()
    }

    fun updateUserPasswordRepetition(password: String) {
        passwordRepetition = password
    }

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
                                    //TODO: завести класс для ошибок enum или что-то поумнее
                                    errorMessage = when (_state.value.loginType) {
                                        LoginType.EMAIL -> "Аккаунт с такой почтой уже существует"
                                        else -> "Аккаунт с таким телефоном уже существует"
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
                                //TODO: завести класс для ошибок enum или что-то поумнее
                                errorMessage = "Неверный код"
                            )
                        }
                    }
                }
            }

            LoginStep.PASSWORD -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val refreshToken = repository.sendUserData(_state.value)
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
        if (!_state.value.canGoBack) return

        _state.update {
            it.copy(
                currentStep = when (it.currentStep) {
                    LoginStep.PASSWORD -> LoginStep.LOGIN
                    LoginStep.VERIFICATION -> LoginStep.LOGIN
                    else -> it.currentStep
                }
            )
        }
        validateCurrentStep()
    }
}