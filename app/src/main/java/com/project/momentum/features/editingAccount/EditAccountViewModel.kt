package com.project.momentum.features.editingAccount

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.project.momentum.features.account.viewmodel.AccountInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EditAccountFields (
    val login: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val profilePhotoURL: String? = null
)

sealed class EditAccountState {
    abstract val fields: EditAccountFields

    data class Loading(
        override val fields: EditAccountFields = EditAccountFields()
    ) : EditAccountState()
    
    data class Error(
        override val fields: EditAccountFields,
        val loginError: String?,
        val emailError: String?,
        val phoneError: String?
    ) : EditAccountState()
    
    data class Content(
        override val fields: EditAccountFields
    ) : EditAccountState()
}

//@HiltViewModel
class EditAccountViewModel: ViewModel() {
    private var _state = MutableStateFlow<EditAccountState>(
        EditAccountState.Loading()
    )
    val state: StateFlow<EditAccountState> = _state.asStateFlow()

    fun setContent() {
        _state.update { EditAccountState.Content(it.fields) }
    }

    fun setLoading() {
        _state.update { EditAccountState.Loading(it.fields) }
    }

    fun setError(
        loginError: String? = null,
        emailError: String? = null,
        phoneError: String? = null
    ) {
        _state.update {
            EditAccountState.Error(
                it.fields,
                loginError = loginError,
                emailError = emailError,
                phoneError = phoneError
            )
        }
    }

    private fun updateState(transform: (EditAccountFields) -> EditAccountFields) {
        _state.update {
            val newFields = transform(it.fields)
            when (it) {
                is EditAccountState.Content -> it.copy(fields = newFields)
                is EditAccountState.Error -> it.copy(fields = newFields)
                is EditAccountState.Loading -> it.copy(fields = newFields)
            }
        }
    }

    fun updateLogin(newLogin: String) {
        updateState {
            it.copy(login = newLogin)
        }
    }

    fun updateEmail(newEmail: String) {
        updateState {
            it.copy(email = newEmail)
        }
    }

    fun updatePhone(newPhone: String) {
        updateState {
            it.copy(phone = newPhone)
        }
    }

    //TODO: add update profile photo
    fun updateProfilePhoto(profilePhotoURL: String) {
        updateState {
            it.copy(profilePhotoURL = profilePhotoURL)
        }
    }
}