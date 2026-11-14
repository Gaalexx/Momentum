package com.project.momentum.data.auth.vk

import android.util.Log
import com.project.momentum.data.auth.deviceinfo.DeviceInfo
import com.project.momentum.data.auth.foreignidauth.ForeignIdAuth
import com.project.momentum.data.auth.remote.AuthorizeVKRequestDTO
import com.project.momentum.data.auth.remote.RegistrationAPI
import com.project.momentum.domain.auth.model.LoginType
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import jakarta.inject.Inject
import kotlinx.coroutines.CompletableDeferred

class VkIdAuthDataSource @Inject constructor(
    private val vkid: VKID,
    private val client: RegistrationAPI,
    private val deviceInfo: DeviceInfo
) : ForeignIdAuth {

    override val type = LoginType.VKID

    private companion object {
        val VKID_AUTH_SCOPES = setOf("email", "friends", "photos", "video")
    }

    override suspend fun auth(): String? {
        val accessToken = requestAccessToken() ?: return null
        val response = client.authorizeWithVK(
            AuthorizeVKRequestDTO(
                vkAccessToken = accessToken,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.jwt
    }

    private suspend fun requestAccessToken(): String? {
        val token = CompletableDeferred<String?>()

        vkid.authorize(
            callback = object : VKIDAuthCallback {
                override fun onAuth(accessToken: AccessToken) {
                    token.complete(accessToken.token)
                }

                override fun onFail(fail: VKIDAuthFail) {
                    Log.e("VkAuth", "VKID auth failed: ${fail.description}")
                    token.complete(null)
                }
            },
            params = VKIDAuthParams {
                scopes = VKID_AUTH_SCOPES
                useOAuthProviderIfPossible = false
                oAuth = OAuth.VK
            },
        )
        return token.await()
    }
}
