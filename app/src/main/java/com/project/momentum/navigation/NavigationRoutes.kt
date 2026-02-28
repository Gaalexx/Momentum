package com.project.momentum.navigation

import kotlinx.serialization.Serializable

// Все маршруты - просто сериализуемые классы
@Serializable
sealed class NavRoutes {
    // Экраны без аргументов
    @Serializable data object Camera : NavRoutes()
    @Serializable data object Recorder : NavRoutes()
    @Serializable data object Gallery : NavRoutes()
    @Serializable data object Friends : NavRoutes()

    // Экраны с аргументами
    @Serializable data class Account(val backTo: String) : NavRoutes()
    @Serializable data class Settings(val backTo: String) : NavRoutes()
    @Serializable data class PreviewPhoto(val url: String) : NavRoutes()
    @Serializable data class SendPhoto(val uri: String) : NavRoutes()
}