package com.project.momentum.domain.settings.model

data class ServerSettings(
    val inAppNotifications: Boolean = true,
    val publicationsEnabled: Boolean = true,
    val reactionsEnabled: Boolean = true,
    val friendRequestEnabled: Boolean = true,
    val defaultThemeEnabled: Boolean = true,
)
