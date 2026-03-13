package com.project.momentum.features.auth.features

import com.project.momentum.BuildConfig
import com.project.momentum.features.auth.models.dto.CheckResponseDTO
import com.project.momentum.features.auth.models.dto.EmailResponse
import com.project.momentum.network.di.EmailChecker
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailCheckerAPI @Inject constructor(
    @EmailChecker private val client: HttpClient
) {
    suspend fun checkEmail(email: String): EmailResponse {
        val response: EmailResponse = client.get("check") {
            url {
                parameters.append("access_key", BuildConfig.API_KEY)
                parameters.append("email", email)
            }
        }.body<EmailResponse>()

        return response
    }
}