package com.project.momentum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Backend
    fun provideBackendClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "193.233.20.47/api/momentum"
                }
                contentType(ContentType.Application.Json)
            }
        }
    }


    @Provides
    @Singleton
    @S3
    fun provideS3Client(): HttpClient = HttpClient(OkHttp)
}


@Serializable
data class SDTO( // пример DTOшки
    val text: String
) {

}

@Serializable
data class LoginResponseDTO(
    val jwt: String,
)

@Serializable
data class RegisterUserRequestDTO(
    val email: String?,
    val phone: String?,
    val password: String,
)

suspend fun gt(): List<SDTO> {  // пример GET запроса
    val httpClient =
        NetworkModule.provideBackendClient() // должно передаваться в репозиторий через @inject
    val ans = httpClient.get("/w").body<List<SDTO>>()
    return ans
}

suspend fun pst(): LoginResponseDTO { // пример post запроса
    val toSend = RegisterUserRequestDTO("test", "test", "test")
    val httpClient =
        NetworkModule.provideBackendClient() // должно передаваться в репозиторий через @inject
    val response: LoginResponseDTO = httpClient.post("/register") {
        setBody(toSend)
    }.body()
    return response
}
