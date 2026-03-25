package com.project.momentum.features.settings.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServerSettingsStateDTO (
    val inAppNotifications: Boolean = false,
    val publicationsEnabled: Boolean = false,
    val reactionsEnabled: Boolean = false,
    val recommendToContacts: Boolean = false,
    val allowAddFromAnyone: Boolean = false,
)

@Serializable
data class LocalSettingsStateDTO (
    val confirmBeforePosting: Boolean = false
)