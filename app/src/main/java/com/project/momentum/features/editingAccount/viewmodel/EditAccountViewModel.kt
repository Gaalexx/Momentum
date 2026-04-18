package com.project.momentum.features.editingAccount.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.repo.AccountRepository
import com.project.momentum.features.auth.features.EmailChecker
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.network.s3.S3InteractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


enum class ErrorType {
    ALREADY_EXIST, WRONG_FORMAT, NOT_EXIST
}

enum class PasswordErrorType {
    TOO_SHORT, NO_DIGITS, NO_LOWERCASE_LETTERS, NO_UPPERCASE_LETTERS
}

@Serializable
data class AccountInfo (
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val profilePhotoURL: String? = null
)

data class EditAccountFields (
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val profilePhotoURL: Uri? = null
)

data class EditAccountErrorFields (
    val usernameError: ErrorType?,
    val emailError: ErrorType?,
    val phoneError: ErrorType?
)

sealed class EditAccountState {
    abstract val fields: EditAccountFields

    fun getErrorForUsername(): ErrorType? = (this as? Error)?.errorFields?.usernameError
    fun getErrorForEmail(): ErrorType? = (this as? Error)?.errorFields?.emailError
    fun getErrorForPhone(): ErrorType? = (this as? Error)?.errorFields?.phoneError

    data class Loading(
        override val fields: EditAccountFields = EditAccountFields()
    ) : EditAccountState()
    
    data class Error(
        override val fields: EditAccountFields,
        val errorFields: EditAccountErrorFields
    ) : EditAccountState()
    
    data class Content(
        override val fields: EditAccountFields
    ) : EditAccountState()
}

data class AvatarInfo(
    val uri: Uri,
    val mimeType: String,
    val size: Long,
)

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val emailChecker: EmailChecker,
    private val repo: AccountRepository,
    private val uploaderRepo: S3InteractionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var _state = MutableStateFlow<EditAccountState>(
        EditAccountState.Loading(fields = EditAccountFields())
    )
    val state: StateFlow<EditAccountState> = _state.asStateFlow()
    var currentUserData = AccountInfo()
        private set

    init {
        viewModelScope.launch {
            update()
            setContent()
        }
    }

    private val _navigationEvents = MutableSharedFlow<NavEvent>()
    val navigationEvents: SharedFlow<NavEvent> = _navigationEvents.asSharedFlow()

    suspend fun update() {
        val userdata = repo.getMyInfo() ?: return  //TODO: handle error

        currentUserData = AccountInfo(
            username = userdata.name,
            email = userdata.email,
            phone = userdata.phone,
            profilePhotoURL = userdata.profilePhotoURL,
        )
    }

    fun next() {
        viewModelScope.launch {
            validateFields()

            if (_state.value !is EditAccountState.Error) {
                //TODO: update user info on client
                try {
                    if (_state.value.fields.username != null ||
                        _state.value.fields.email != null ||
                        _state.value.fields.phone != null)
                        repo.updateUserInfo(_state.value.fields)
                    selectPhoto(context, _state.value.fields.profilePhotoURL)
//                    update()
                    clearState()
                    _navigationEvents.emit(NavEvent.NavigateToNextScreen)
                } catch (e: Exception) {
                    //TODO: обработка ошибки сети
                    Log.e("Momentum", e.message.toString())
                }
                setContent()
            }
        }
    }



    fun selectPhoto(context: Context, uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch {
            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            val size = context.contentResolver.openFileDescriptor(uri, "r")
                ?.use { pfd -> pfd.statSize }?.takeIf { it >= 0 } ?: 0L

            uploaderRepo.sendAvatar(
                AvatarInfo(
                    uri = uri,
                    mimeType = mimeType,
                    size = size
                )
            )
        }
    }

    suspend fun validateFields() {
        val fields = _state.value.fields
        setLoading()

        setError(
            usernameError =
                if (!fields.username.isNullOrBlank() && !isValidUsername(fields.username)) ErrorType.WRONG_FORMAT
                else null,
            emailError =
                if (!fields.email.isNullOrBlank() && !isValidEmail(fields.email)) ErrorType.WRONG_FORMAT
                else null,
            phoneError =
                if (!fields.phone.isNullOrBlank() && !isValidPhone(fields.phone)) ErrorType.WRONG_FORMAT
                else null
        )
        if (_state.value is EditAccountState.Error) return

        try {
            if (!fields.email.isNullOrBlank() && emailChecker.checkEmail(fields.email)) {
                setError(emailError = ErrorType.NOT_EXIST)
                return
            }

            val errors = repo.checkUserInfoDB(fields)
            setError(
                usernameError = errors.usernameError,
                emailError = errors.emailError,
                phoneError = errors.phoneError
            )
        } catch (e: Exception) {
            //TODO: обработка ошибки сети
            Log.e("Momentum", e.message.toString())
        }
    }

    fun isValidUsername(username: String): Boolean {
        return true
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        //TODO: проверка на валидность номера
        return Patterns.PHONE.matcher(phone).matches()
    }

    fun isValidPassword(password: String): PasswordErrorType? {
        if (password.length < 8) {
            return PasswordErrorType.TOO_SHORT
        }
        return when {
            !password.contains(Regex("[0-9]")) -> PasswordErrorType.NO_DIGITS
            !password.contains(Regex("[a-z]")) -> PasswordErrorType.NO_LOWERCASE_LETTERS
            !password.contains(Regex("[A-Z]")) -> PasswordErrorType.NO_UPPERCASE_LETTERS
            else -> null
        }
    }

    fun setContent() {
        _state.update { EditAccountState.Content(it.fields) }
    }

    fun setLoading() {
        _state.update { EditAccountState.Loading(it.fields) }
    }

    fun setError(
        usernameError: ErrorType? = null,
        emailError: ErrorType? = null,
        phoneError: ErrorType? = null
    ) {
        if (usernameError == null && emailError == null && phoneError == null) {
//            setContent() //TODO: переделать из-за этого экран загрузки пропадает раньше чем надо
            return
        }
        _state.update { state ->
            val currentErrors = (state as? EditAccountState.Error)?.errorFields
            EditAccountState.Error(
                fields = state.fields,
                errorFields = EditAccountErrorFields(
                    usernameError = currentErrors?.usernameError ?: usernameError,
                    emailError = currentErrors?.emailError ?: emailError,
                    phoneError = currentErrors?.phoneError ?: phoneError
                )
            )
        }
    }

    private fun clearState() {
        updateState{
            it.copy(
                username = null,
                email = null,
                phone = null,
                profilePhotoURL = null
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

    fun updateLogin(newUsername: String) {
        updateState {
            it.copy(username = newUsername)
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
    fun updateProfilePhoto(profilePhotoURL: Uri) {
        updateState {
            it.copy(profilePhotoURL = profilePhotoURL)
        }
    }
}