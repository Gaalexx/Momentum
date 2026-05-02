package com.project.momentum.features.account.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountInformationDTO(
    val userId: String,
    val email: String,
    val name: String,
    val phone: String?,
    val profilePhotoURL: String?,
    val hasPremium: Boolean
)

@Serializable
data class EditAccountDTO ( // maybe in future AccountInformationDTO
    val username: String?,
    val email: String?,
    val phone: String?,
    val profilePhotoURL: String?
)

@Serializable
data class CheckUserInfoIsFreeRequestDTO(
    val username: String?,
    val email: String?,
    val phone: String?,
)

@Serializable
data class CheckUserInfoIsFreeResponseDTO(
    val isUsernameFree: Boolean?,
    val isEmailFree: Boolean?,
    val isPhoneFree: Boolean?,
)