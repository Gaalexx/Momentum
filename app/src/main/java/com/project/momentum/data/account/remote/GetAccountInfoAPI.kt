package com.project.momentum.data.account.remote

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.account.remote.AccountInformationDTO
import com.project.momentum.data.account.remote.CheckUserInfoIsFreeRequestDTO
import com.project.momentum.data.account.remote.CheckUserInfoIsFreeResponseDTO
import com.project.momentum.data.account.remote.EditAccountDTO
import com.project.momentum.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class GetAccountInfoAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
) {

    suspend fun getAccountInformation(): AccountInformationDTO {
        val response = client.post("me") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<AccountInformationDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }

    suspend fun checkUserInfoIsFree(userInfo: CheckUserInfoIsFreeRequestDTO): CheckUserInfoIsFreeResponseDTO {
        val response = client.post("check-userinfo-is-free") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(userInfo)
        }.body<CheckUserInfoIsFreeResponseDTO>()

        return response
    }

    suspend fun sendNewUserInfo(userInfo: EditAccountDTO): EditAccountDTO {
        val response = client.post("update-user-info") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(userInfo)
        }.body<EditAccountDTO>()

        return response
    }
}