package com.project.momentum.features.friends.api

import android.util.Log
import com.example.Models.FriendRequestActionDTO
import com.example.Models.FriendRequestCreateByEmailDTO
import com.example.Models.FriendRequestWithUserDetailsDTO
import com.example.Models.FriendshipListDTO
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsInfoAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
) {

    suspend fun deleteFriendshipWith(
        id: String
    ): Boolean {
        val result = client.delete("friends/${id}") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }


        return when (result.status) {
            HttpStatusCode.OK -> true
            else -> false
        }
    }

    suspend fun createRequestWithEmail(
        email: String
    ) {
        val result = client.post("friends/request/by-email") {
            setBody(FriendRequestCreateByEmailDTO(email))
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }.body<FriendRequestActionDTO>()
    }

    suspend fun createRequestWithLogin(
        login: String
    ) {
        val result = client.post("friends/request/by-name") {
            setBody(FriendRequestCreateByEmailDTO(login))
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }.body<FriendRequestActionDTO>()
    }

    suspend fun getIncomingRequests(): List<FriendRequestWithUserDetailsDTO> {
        val result = client.get("friends/requests/incoming") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }

        if (result.status == HttpStatusCode.OK) {
            return result.body<List<FriendRequestWithUserDetailsDTO>>()
        } else {
            throw Exception()
        }
    }

    suspend fun acceptIncomingRequest(
        requestId: String
    ): FriendRequestActionDTO {
        val result = client.patch("friends/request/${requestId}/accept") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }

        if (result.status == HttpStatusCode.OK) {
            return result.body<FriendRequestActionDTO>()
        } else {
            throw Exception()
        }
    }

    suspend fun rejectIncomingRequest(
        requestId: String
    ): FriendRequestActionDTO {
        val result = client.patch("friends/request/${requestId}/reject") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }

        if (result.status == HttpStatusCode.OK) {
            return result.body<FriendRequestActionDTO>()
        } else {
            throw Exception()
        }
    }

    suspend fun getFriends(): FriendshipListDTO {
        val result = client.get("friends") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }

        return if (result.status == HttpStatusCode.OK) {
            Log.d("Friends", "${result.body<FriendshipListDTO>()}")
            result.body<FriendshipListDTO>()
        } else {
            Log.e("Friends", result.body())
            FriendshipListDTO(listOf())
        }
    }

    suspend fun deleteRequest(
        requestId: String
    ): FriendRequestActionDTO {
        val result = client.delete("friends/request/${requestId}") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }.body<FriendRequestActionDTO>()

        return result
    }


}