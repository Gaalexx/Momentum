package com.project.momentum.features.friends.api

import android.util.Log
import com.project.momentum.features.friends.api.dtos.VkFriend
import com.project.momentum.features.friends.api.requestclasses.GetFriendsRequest
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Singleton
class VkCommandAPI @Inject constructor() {

    suspend fun getFriendsFromVK(): List<VkFriend> {
        return suspendCoroutine { continuation ->
            VK.execute(
                GetFriendsRequest(),
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