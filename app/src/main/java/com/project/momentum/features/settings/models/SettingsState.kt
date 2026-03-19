package com.project.momentum.features.settings.models

data class SettingsState (
    val inAppNotifications: Boolean = false,
    val publicationsEnabled: Boolean = false,
    val reactionsEnabled: Boolean = false,
    val recommendToContacts: Boolean = false,
    val allowAddFromAnyone: Boolean = false,
    val confirmBeforePosting: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)