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

sealed interface DeleteEvent {
    data object nextStep : DeleteEvent
    data object previousStep : DeleteEvent
    data object onConfirmDelete : DeleteEvent
    data object onCancelDelete : DeleteEvent
    //data class updateUserPassword(val password: String) : DeleteEvent
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
    private val _navigationEvents = MutableSharedFlow<NavEvent>()
    val navigationEvents: SharedFlow<NavEvent> = _navigationEvents.asSharedFlow()

    fun onEvent(event: DeleteEvent) {
        when (event) {
            is DeleteEvent.nextStep -> nextStep()
            is DeleteEvent.previousStep -> previousStep()
            is DeleteEvent.onConfirmDelete -> onConfirmDelete()
            is DeleteEvent.onCancelDelete -> onCancelDelete()
            is DeleteEvent.updatePasswordFstTextField -> {updatePasswordFstTextField(event.password)}
            is DeleteEvent.updatePasswordScdTextField -> {updatePasswordScdTextField(event.password)}
            is DeleteEvent.updateUserCode -> {updateUserCode(event.code)}
        }
    }

    private fun nextStep() {
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

    private fun onCancelDelete() {
        _state.update { it.copy(showConfirmationDialog = false) }
    }

    private fun updatePasswordFstTextField(password: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(passwordFstTextField = password)
            )
        }
    }

    private fun updatePasswordScdTextField(password: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(passwordScdTextField = password)
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
}