package com.project.momentum.features.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.ui.AccountScreen
import com.project.momentum.features.account.usecases.GetInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountInfoState(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val profilePhotoURL: String? = null,
    val hasPremium: Boolean = false,
)


sealed interface AccountInfoEvent {
    data object GetInfo : AccountInfoEvent
}

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val infoUseCase: GetInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AccountInfoState>(AccountInfoState(
        "", "", "")) //TODO: isLoading
    val state = _state.asStateFlow()

    init {
        getAccountInfo()
    }

    fun onEvent(event: AccountInfoEvent) {
        when (event) {
            is AccountInfoEvent.GetInfo -> getAccountInfo()
        }
    }

    private fun getAccountInfo() {
        viewModelScope.launch {
            val info = infoUseCase.getMyInfo()
            if (info != null) {
                _state.value = AccountInfoState(
                    userId = info.userId,
                    email = info.email,
                    name = info.name,
                    phone = info.phone,
                    profilePhotoURL = info.profilePhotoURL,
                    hasPremium = info.hasPremium
                )
            }
        }
    }
}