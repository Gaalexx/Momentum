package com.project.momentum.navigation

import androidx.navigation3.runtime.NavKey
import com.project.momentum.features.account.models.PostData
import kotlinx.serialization.Serializable

// Все маршруты - просто сериализуемые классы
@Serializable
sealed class NavRoutes : NavKey {
    // Экраны без аргументов
    @Serializable
    data object Camera : NavRoutes()

    @Serializable
    data object Recorder : NavRoutes()

    @Serializable
    data object Gallery : NavRoutes()

    @Serializable
    data object Friends : NavRoutes()

    @Serializable
    data object Premium : NavRoutes()


    @Serializable
    data object DeleteAccountCheckPassword : NavRoutes()

    @Serializable
    data object DeleteAccountCheckCode : NavRoutes()

    @Serializable
    data object DeleteAccountConfirmation : NavRoutes()

    //Экраны регистрации
    @Serializable
    data object RegistrationLogin : NavRoutes()

    @Serializable
    data object RegistrationCode : NavRoutes()

    @Serializable
    data object RegistrationPassword : NavRoutes()

    //Экраны входа
    @Serializable
    data object AuthorizationLogin : NavRoutes()

    @Serializable
    data object AuthorizationCode : NavRoutes()

    @Serializable
    data object AuthorizationPassword : NavRoutes()

    @Serializable
    data object AuthorizationPasswordRecovery : NavRoutes()

    // Экраны с аргументами
    @Serializable
    data class Account(val backTo: String) : NavRoutes()

    @Serializable
    data class Settings(val backTo: String) : NavRoutes()

    //
//    @Serializable
//    data class PreviewPhoto(val postUrl: String) : NavRoutes()
    @Serializable
    data class PreviewPhoto(val post: PostData) : NavRoutes()

    @Serializable
    data class SendPhoto(val uri: String) : NavRoutes()
}