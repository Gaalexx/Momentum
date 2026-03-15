package com.project.momentum.features.editingAccount

import androidx.compose.runtime.collectAsState
import com.project.momentum.features.account.viewmodel.AccountInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


//@HiltViewModel
class EditAccountViewModel {
    private var _state = MutableStateFlow(AccountInfoState("", ""))
    val state: StateFlow<AccountInfoState> = _state.asStateFlow()

    //TODO
}