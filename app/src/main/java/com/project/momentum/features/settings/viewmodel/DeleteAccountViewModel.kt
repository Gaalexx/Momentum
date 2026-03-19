package com.project.momentum.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.settings.models.DeleteAccountState
import com.project.momentum.features.settings.models.DeleteAccountStep
import com.project.momentum.features.auth.models.NavEvent
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

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val repository: DeleteAccountRepository
) : ViewModel() {
    private var _state = MutableStateFlow(DeleteAccountState())
    val state: StateFlow<DeleteAccountState> = _state.asStateFlow()
    private val _navigationEvents = MutableSharedFlow<NavEvent>()
    val navigationEvents: SharedFlow<NavEvent> = _navigationEvents.asSharedFlow()



    fun isValidCode(): Boolean {
        return true
    }

    fun nextStep() {
        if (!_state.value.isStepValid) return

        when (_state.value.currentStep) {
            DeleteAccountStep.PASSWORD -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    val refreshToken = repository.login(_state.value)
                    if (refreshToken != null) {
                        //TODO: куда-то сохранить или что-то сделать
                        _state.update {
                            it.copy(
                                currentStep = DeleteAccountStep.VERIFICATION
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

            DeleteAccountStep.VERIFICATION -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    if (repository.checkUserCode(_state.value)) {
                        _state.update {
                            it.copy(
                                currentStep = DeleteAccountStep.DELETE_ACCOUNT,
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

    fun previousStep() {
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

    fun onCodeAuthorization() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            repository.sendAuthorizationCode(_state.value)
            _state.update {
                it.copy(
                    currentStep = DeleteAccountStep.VERIFICATION,
                    isError = false,
                    isLoading = false
                )
            }
            _navigationEvents.emit(NavEvent.NavigateToNextSubScreen)
        }
    }

    fun onConfirmDelete() {
        _state.update { it.copy(isLoading = true, showConfirmationDialog = false) }

        viewModelScope.launch {
            val success = repository.deleteAccount(_state.value)

            if (success) {
                _state.update {
                    it.copy(
                        currentStep = DeleteAccountStep.COMPLETED,
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
                        errorMessage = "Не удалось удалить аккаунт"
                    )
                }
            }
        }
    }

    fun onCancelDelete() {
        _state.update { it.copy(showConfirmationDialog = false) }
    }

    fun updateUserPassword(password: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(password = password)
            )
        }
    }

}