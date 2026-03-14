package com.project.momentum.features.account.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountInformationDTO(
    val name: String,
    val accountPhotoURL: String?
)