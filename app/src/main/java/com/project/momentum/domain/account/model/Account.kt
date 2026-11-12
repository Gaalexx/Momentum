package com.project.momentum.domain.account.model

data class Account(
    val userId: String,
    val email: String,
    val name: String,
    val phone: String?,
    val profilePhotoURL: String?,
    val hasPremium: Boolean
)

data class AccountChanges(
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val profilePhotoURL: String? = null
)

data class AccountAvailability(
    val isUsernameFree: Boolean,
    val isEmailFree: Boolean,
    val isPhoneFree: Boolean
)
