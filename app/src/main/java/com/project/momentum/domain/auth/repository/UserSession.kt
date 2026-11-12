package com.project.momentum.domain.auth.repository

/** Read-only identity of the signed-in user, without exposing credential storage. */
interface UserSession {
    fun getUserId(): String?
}
