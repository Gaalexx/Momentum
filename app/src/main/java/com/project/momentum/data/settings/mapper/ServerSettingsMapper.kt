package com.project.momentum.data.settings.mapper

import com.project.momentum.data.settings.remote.ServerSettingsStateDTO
import com.project.momentum.domain.settings.model.ServerSettings

fun ServerSettingsStateDTO.toDomain(): ServerSettings = ServerSettings(
    inAppNotifications = inAppNotifications,
    publicationsEnabled = publicationsEnabled,
    reactionsEnabled = reactionsEnabled,
    friendRequestEnabled = friendRequestEnabled,
    defaultThemeEnabled = defaultThemeEnabled,
)
