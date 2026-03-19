package com.project.momentum.features.settings.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class SwitchStateDTO(
    val switchState: Boolean,
)