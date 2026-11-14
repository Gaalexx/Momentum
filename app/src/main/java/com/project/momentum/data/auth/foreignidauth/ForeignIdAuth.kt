package com.project.momentum.data.auth.foreignidauth

import com.project.momentum.domain.auth.model.LoginType

interface ForeignIdAuth {
    val type: LoginType

    suspend fun auth(): String?
}
