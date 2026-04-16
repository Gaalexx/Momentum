package com.project.momentum.data.remote

import android.util.Log
import com.project.momentum.features.auth.models.dto.GetJWTDTO
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton

interface IAuthAPI {
    suspend fun tryAuth(token: String): GetJWTDTO?
}

@Singleton
class AuthAPI @Inject constructor(
    @Backend private val client: HttpClient
) : IAuthAPI {

    override suspend fun tryAuth(token: String): GetJWTDTO {
        val result = client.post("auth") {
            setBody(GetJWTDTO(token))
        }

        return try {
                result.body<GetJWTDTO>()
            } catch (e: ConnectException) {
                Log.e("tryAuth", result.toString())
                GetJWTDTO(null)
            }
    }


}