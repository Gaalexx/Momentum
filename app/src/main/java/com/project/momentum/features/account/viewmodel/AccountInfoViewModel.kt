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
    val name: String,
    val profilePhotoURL: String?
)


sealed interface AccountInfoEvent {
    data object GetInfo : AccountInfoEvent
}

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val infoUseCase: GetInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AccountInfoState>(AccountInfoState("", null))
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
                _state.value = AccountInfoState(info.name, info.accountPhotoURL)
            }
        }
    }
}