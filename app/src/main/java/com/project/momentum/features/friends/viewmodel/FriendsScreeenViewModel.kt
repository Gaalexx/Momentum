package com.project.momentum.features.friends.viewmodel

import android.R
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.project.momentum.features.friends.repo.FriendsRepository
import com.project.momentum.features.friends.ui.Friend
import com.project.momentum.features.friends.ui.FriendRequest
import com.project.momentum.features.friends.ui.User
import com.project.momentum.features.friends.ui.UserNew
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.request.request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


data class FriendsScreenState(
    val friends: List<UserNew>,
    val friendRequests: List<FriendRequest>,
    val isLoading: Boolean,
    val showPage: Boolean,
    val addFriendQuery: String,
    val searchQuery: String

)

sealed interface FriendsScreenEvent {
    data class AcceptRequest(val request: FriendRequest) : FriendsScreenEvent
    data class RejectRequest(val request: FriendRequest) : FriendsScreenEvent
    data class CreateEmailRequest(val email: String) : FriendsScreenEvent
    data object GetFriends : FriendsScreenEvent
    data object GetRequests : FriendsScreenEvent

    data class ShowPageEvent(val newValue: Boolean) : FriendsScreenEvent

    data class SearchQueryChange(val newValue: String) : FriendsScreenEvent

    data class AddFriendQueryChange(val newValue: String) : FriendsScreenEvent
}

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repo: FriendsRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<FriendsScreenState>(FriendsScreenState(listOf(), listOf(), false, false, "", ""))
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
            is FriendsScreenEvent.CreateEmailRequest -> createFriendRequestWithEmail(event)
            is FriendsScreenEvent.ShowPageEvent -> onShowPageChange(event)
            is FriendsScreenEvent.AddFriendQueryChange -> onAddFriendQueryChange(event)
            is FriendsScreenEvent.SearchQueryChange -> onSearchQueryChange(event)
        }
    }

    private fun onShowPageChange(value: FriendsScreenEvent.ShowPageEvent) {
        _state.update { it.copy(showPage = value.newValue) }
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
                    UserNew(
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

    private fun createFriendRequestWithEmail(request: FriendsScreenEvent.CreateEmailRequest) {
        viewModelScope.launch {
            repo.createFriendRequest(FriendsRepository.RequestBy.ByEmail(request.email))
        }
    }

}