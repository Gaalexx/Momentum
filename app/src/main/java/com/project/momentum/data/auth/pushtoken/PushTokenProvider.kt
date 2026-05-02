package com.project.momentum.data.auth.pushtoken

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface PushTokenProvider {
    suspend fun getToken(): String?
}

@Singleton
class FirebasePushTokenProvider @Inject constructor() : PushTokenProvider {
    override suspend fun getToken(): String? =
        runCatching {
            FirebaseMessaging.getInstance().token.await()
        }.getOrNull()

}