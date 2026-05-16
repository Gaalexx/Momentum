package com.project.momentum.features.friends.api

import android.util.Log
import com.project.momentum.features.friends.api.dtos.VkFriend
import com.project.momentum.features.friends.api.requestclasses.GetFriendsRequest
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.id.VKID
import com.vk.id.vksdksupport.withVKIDToken
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Singleton
class VkCommandAPI @Inject constructor() {

    suspend fun getFriendsFromVK(): List<VkFriend> {
        val token = VKID.instance.accessToken
        Log.d("VK_DEBUG", "accessToken is null: ${token == null}")
        if (token != null) {
            Log.d("VK_DEBUG", "userID: ${token.userID}")
            Log.d("VK_DEBUG", "scopes: ${token.scopes}")
            Log.d("VK_DEBUG", "expireTime: ${token.expireTime}, now: ${System.currentTimeMillis()}")
            Log.d("VK_DEBUG", "userData.firstName: ${token.userData.firstName}")
            Log.d("VK_DEBUG", "token (first 10 chars): ${token.token.take(10)}...")
        }

        return suspendCoroutine { continuation ->
            VK.execute(
                GetFriendsRequest().withVKIDToken(),
                object : VKApiCallback<List<VkFriend>> {
                    override fun fail(error: Exception) {
                        Log.e("VK", "Failed to load friends", error)

                        continuation.resumeWithException(error)
                    }

                    override fun success(result: List<VkFriend>) {
                        result.forEach { friend ->
                            Log.d(
                                "VK",
                                "${friend.firstName} ${friend.lastName}, photo=${friend.photo200}"
                            )
                        }
                        continuation.resume(result)
                    }

                }
            )
        }

    }
}