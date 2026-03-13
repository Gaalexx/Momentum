package com.project.momentum.features.auth.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailResponse (
    val email: String = "",
    val didYouMean: String = "",
    val user: String = "",
    val domain: String = "",
    val formatValid: Boolean = false,   // format of email or not
    val mxFound: Boolean = false,       // domain can receive emails
    val smtpCheck: Boolean = false,     // exists
    val catchAll: Boolean = false,
    val role: Boolean = false,
    val disposable: Boolean = false,
    val free: Boolean = false,
    val score: Double = 0.0,
)