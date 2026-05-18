package com.project.momentum.features.account.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.ui.AccountScreen
import com.project.momentum.features.account.usecases.GetInfoUseCase
import com.project.momentum.features.contentcreation.viewmodel.UploadState
import com.project.momentum.features.editingAccount.viewmodel.AvatarInfo
import com.project.momentum.network.s3.MediaType
import com.project.momentum.network.s3.PostInformation
import com.project.momentum.network.s3.S3InteractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountInfoState(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val profilePhotoURL: String? = null,
    val hasPremium: Boolean = false,
    val isShowingActionsDialog: Boolean = false,
    val selectedPost: String? = null
)


sealed interface AccountInfoEvent {
    data object GetInfo : AccountInfoEvent
    data class OnShowActionsDialog(val isShowing: Boolean) : AccountInfoEvent
    data class SelectPost(val post: String?) : AccountInfoEvent
}

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val infoUseCase: GetInfoUseCase,
    private val uploaderRepo: S3InteractionRepository
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
            is AccountInfoEvent.OnShowActionsDialog -> showActionsDialog(event)
            is AccountInfoEvent.SelectPost -> selectPost(event)
        }
    }

    private fun selectPost(event: AccountInfoEvent.SelectPost) {
        _state.update {
            it.copy(selectedPost = event.post)
        }
    }

    private fun showActionsDialog(event: AccountInfoEvent.OnShowActionsDialog) {
        _state.update {
            it.copy(isShowingActionsDialog = event.isShowing)
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