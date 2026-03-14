package com.project.momentum.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import com.project.momentum.BuildConfig
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
//import java.util.logging.Logger

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
                url(BuildConfig.BACKEND_BUILD_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }


    @Provides
    @Singleton
    @EmailChecker
    fun provideEmailCheckerClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
            install(HttpCallValidator) {
                validateResponse { response ->
                    when (response.status.value) {
                        in 300..399 -> throw RedirectResponseException(
                            response,
                            "Redirect error: ${response.status}")
                        in 400..499 -> throw ClientRequestException(
                            response,
                            "Client error: ${response.status}")
                        in 500..599 -> throw ServerResponseException(
                            response,
                            "Server error: ${response.status}")
                    }
                }
            }
            defaultRequest {
                url(BuildConfig.EMAIL_CHECKER)
                contentType(ContentType.Application.Json)
            }
        }
    }


    @Provides
    @Singleton
    @S3
    fun provideS3Client(): HttpClient = HttpClient(OkHttp)
}
