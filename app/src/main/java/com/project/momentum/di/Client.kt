package com.project.momentum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
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
            }
        }
    }
}


@Serializable
data class SDTO( //для теста :)
    val text: String
) {

}

suspend fun gt(): List<SDTO> {
    val httpClient = NetworkModule.provideHttpClient()
    val ans = httpClient.get("/w").body<List<SDTO>>()
    return ans
}