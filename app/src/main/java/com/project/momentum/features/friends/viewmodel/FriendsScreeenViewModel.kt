package com.project.momentum.features.friends.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.project.momentum.R
import com.project.momentum.features.friends.repo.FriendsRepository
import com.project.momentum.features.friends.ui.FriendRequest
import com.project.momentum.features.friends.ui.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


enum class SelectedIndex(val index: Int) {
    EMAIL(0),
    LOGIN(1),
    TELEPHONE(2);

    companion object {
        fun fromIndex(index: Int): SelectedIndex =
            entries.firstOrNull { it.index == index } ?: EMAIL
    }

}

data class FriendsScreenState(
    val friends: List<User>,
    val friendRequests: List<FriendRequest>,
    val isLoading: Boolean = false,
    val showPage: Boolean = false,
    val addFriendQuery: String = "",
    val searchQuery: String = "",
    val selectedIndex: SelectedIndex = SelectedIndex.EMAIL,
    val errorState: Boolean = false,
    @StringRes val errorText: Int? = null
)

sealed interface FriendsScreenEvent {
    data class AcceptRequest(val request: FriendRequest) : FriendsScreenEvent
    data class RejectRequest(val request: FriendRequest) : FriendsScreenEvent
    sealed interface CreateFriendRequest : FriendsScreenEvent {
        val identifier: String

        data class EmailRequest(override val identifier: String) : CreateFriendRequest
        data class LoginRequest(override val identifier: String) : CreateFriendRequest
        data class PhoneRequest(override val identifier: String) : CreateFriendRequest
    }

    data object GetFriends : FriendsScreenEvent
    data object GetRequests : FriendsScreenEvent

    data class ShowPageEvent(val newValue: Boolean) : FriendsScreenEvent

    data class SearchQueryChange(val newValue: String) : FriendsScreenEvent

    data class AddFriendQueryChange(val newValue: String) : FriendsScreenEvent
    data class ChangeSelectedIndex(val newIndex: SelectedIndex) : FriendsScreenEvent
}

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repo: FriendsRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<FriendsScreenState>(
            FriendsScreenState(
                listOf(),
                listOf(),
                false,
                false,
                "",
                ""
            )
        )
    val state = _state.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: FriendsScreenEvent) {
        when (event) {
            is FriendsScreenEvent.AcceptRequest -> acceptRequest(event)
            is FriendsScreenEvent.RejectRequest -> rejectRequest(event)
            is FriendsScreenEvent.GetFriends -> getFriends()
            is FriendsScreenEvent.GetRequests -> getRequests()
            is FriendsScreenEvent.CreateFriendRequest -> createFriendRequest(event)
            is FriendsScreenEvent.ShowPageEvent -> onShowPageChange(event)
            is FriendsScreenEvent.AddFriendQueryChange -> onAddFriendQueryChange(event)
            is FriendsScreenEvent.SearchQueryChange -> onSearchQueryChange(event)
            is FriendsScreenEvent.ChangeSelectedIndex -> onChangeSelectedIndex(event)
        }
    }

    private fun onChangeSelectedIndex(value: FriendsScreenEvent.ChangeSelectedIndex) {
        _state.update { it.copy(selectedIndex = value.newIndex) }
    }

    private suspend fun onChangeSelectedIndexValue(value: SelectedIndex) {
        _state.update { it.copy(selectedIndex = value) }
    }

    private fun onShowPageChange(value: FriendsScreenEvent.ShowPageEvent) {
        _state.update { it.copy(showPage = value.newValue) }
    }

    private suspend fun onShowPageChangeValue(value: Boolean) {
        _state.update { it.copy(showPage = value) }
    }


    private suspend fun onAddFriendQueryChangeValue(value: String) {
        _state.update { it.copy(addFriendQuery = value) }
    }

    private fun onAddFriendQueryChange(value: FriendsScreenEvent.AddFriendQueryChange) {
        _state.update { it.copy(addFriendQuery = value.newValue) }
    }

    private fun onSearchQueryChange(value: FriendsScreenEvent.SearchQueryChange) {
        _state.update { it.copy(searchQuery = value.newValue) }
    }


    private fun loadInitialData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val friends = repo.getAllFriends()
            val requests = repo.getAllRequests()
            _state.value = _state.value.copy(
                friendRequests = requests,
                friends = friends,
                isLoading = false
            )
        }
    }


    private fun acceptRequest(accept: FriendsScreenEvent.AcceptRequest) {
        viewModelScope.launch {
            repo.acceptFriendRequest(accept.request.id)

            _state.value = _state.value.copy(
                friends = _state.value.friends.plus(
                    User(
                        accept.request.userId,
                        accept.request.userName
                    )
                ),
                friendRequests = _state.value.friendRequests.filterNot { it.id == accept.request.id }
            )
        }
    }

    private fun rejectRequest(reject: FriendsScreenEvent.RejectRequest) {
        viewModelScope.launch {
            repo.rejectFriendRequest(reject.request.id)

            _state.value = _state.value.copy(
                friendRequests = _state.value.friendRequests.filterNot { it.id == reject.request.id }
            )
        }
    }

    private fun getFriends() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            _state.value = _state.value.copy(
                friends = repo.getAllFriends(),
                isLoading = false
            )
        }
    }

    private fun getRequests() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                friendRequests = repo.getAllRequests()
            )
        }
    }

    private suspend fun clearError() {
        if (_state.value.errorState || _state.value.errorText != null) {
            _state.update {
                it.copy(
                    errorState = false,
                    errorText = null
                )
            }
        }
    }

    private fun createFriendRequest(request: FriendsScreenEvent.CreateFriendRequest) {
        viewModelScope.launch {
            val res = when (request) {
                is FriendsScreenEvent.CreateFriendRequest.EmailRequest -> repo.createFriendRequest(
                    FriendsRepository.RequestBy.ByEmail(request.identifier)
                )

                is FriendsScreenEvent.CreateFriendRequest.LoginRequest -> repo.createFriendRequest(
                    FriendsRepository.RequestBy.ByLogin(request.identifier)
                )

                is FriendsScreenEvent.CreateFriendRequest.PhoneRequest -> repo.createFriendRequest(
                    FriendsRepository.RequestBy.ByNumber(request.identifier)
                )
            }
            if (!res) {
                _state.update {
                    it.copy(
                        errorState = true,
                        errorText = R.string.person_wasnt_found
                    )
                }
            } else if (_state.value.errorState || _state.value.errorText != null) {
                clearError()
            } else {
                onAddFriendQueryChangeValue("")
                onShowPageChangeValue(false)
            }
        }
    }

}