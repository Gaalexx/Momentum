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
data class SettingsActionDTO(
    val success: Boolean,
    val message: String,
    val newState: ServerSettingsStateDTO? = null
)
@Serializable
data class ChangeInAppNotificationsDTO(
    val inAppNotifications: Boolean
)

@Serializable
data class ChangePublicationsEnabledDTO(
    val publicationsEnabled: Boolean
)

@Serializable
data class ChangeReactionsEnabledDTO(
    val reactionsEnabled: Boolean
)

@Serializable
data class ChangeRecommendToContactsDTO(
    val recommendToContacts: Boolean
)

@Serializable
data class ChangeAllowAddFromAnyoneDTO(
    val allowAddFromAnyone: Boolean
)


@Serializable
data class LocalSettingsStateDTO (
    val confirmBeforePosting: Boolean = false
)