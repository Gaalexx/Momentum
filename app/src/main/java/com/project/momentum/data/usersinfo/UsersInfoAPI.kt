package com.project.momentum.data.usersinfo

import com.project.momentum.features.account.models.AccountInformationDTO
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersInfoAPI @Inject constructor(
    @Backend private val client: HttpClient
) {

    suspend fun userByIdExists(id: String) : Boolean {
        val answer = client.get("users/exists/id/${id}")
        if (answer.status == HttpStatusCode.OK) {
            return answer.body<Boolean>()
        }
        else{
            throw Exception()
        }
    }

    suspend fun userByEmailExists(email: String) : Boolean {
        val answer = client.get("users/exists/email/${email}")
        if (answer.status == HttpStatusCode.OK) {
            return answer.body<Boolean>()
        }
        else{
            throw Exception()
        }
    }

    suspend fun userByPhoneExists(phone: String) : Boolean {
        val answer = client.get("users/exists/phone/${phone}")
        if (answer.status == HttpStatusCode.OK) {
            return answer.body<Boolean>()
        }
        else{
            throw Exception()
        }
    }

    suspend fun userByLoginExists(login: String) : Boolean {
        val answer = client.get("users/exists/phone/${login}")
        if (answer.status == HttpStatusCode.OK) {
            return answer.body<Boolean>()
        }
        else{
            throw Exception()
        }
    }

    suspend fun getUserById(id: String) : AccountInformationDTO {
        val answer = client.get("users/get-info/id/${id}")
        if (answer.status == HttpStatusCode.OK) {
            return answer.body<AccountInformationDTO>()
        }
        else{
            throw Exception()
        }
    }

}