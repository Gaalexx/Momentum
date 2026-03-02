package com.project.momentum.ui.screens.registration

import androidx.lifecycle.ViewModel
import com.project.momentum.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistrationViewModel: ViewModel() {
    private var _userData = MutableStateFlow(UserData())

    val userData: StateFlow<UserData> = _userData.asStateFlow()

    fun updateUserEmail(email: String) {
        _userData.update { currantState ->
            currantState.copy (
                email = email
            )
        }
    }

    fun updateUserPhone(phone: String) {
        _userData.update { currantState ->
            currantState.copy (
                phone = phone
            )
        }
    }

    fun updateUserPassword(password: String) {
        _userData.update { currantState ->
            currantState.copy (
                password = password
            )
        }
    }

}