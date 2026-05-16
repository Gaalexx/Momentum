package com.project.momentum.features.friends.api.requestclasses

import com.project.momentum.features.friends.api.dtos.VkFriend
import com.vk.api.sdk.VKApiJSONResponseParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject


class GetFriendsRequest : ApiCommand<List<VkFriend>>() {

    override fun onExecute(manager: VKApiManager): List<VkFriend> {
        val call = VKMethodCall.Builder()
            .method("friends.get")
            .args("friends", "photo_200,online")
            .args("order", "name")
            .version(manager.config.version)
            .build()

        return manager.execute(call, ResponseParser())
    }

    private class ResponseParser : VKApiJSONResponseParser<List<VkFriend>> {
        override fun parse(responseJson: JSONObject): List<VkFriend> {
            try {
                val response = responseJson.getJSONObject("response")
                val items = response.getJSONArray("items")

                val friends = mutableListOf<VkFriend>()
                for (i in 0 until items.length()) {
                    val friendJson = items.getJSONObject(i)

                    friends += VkFriend(
                        id = friendJson.getLong("id"),
                        firstName = friendJson.getString("first_name"),
                        lastName = friendJson.getString("last_name"),
                        photo200 = friendJson.optString("photo_200", null),
                        online = friendJson.optInt("online", 0) == 1
                    )
                }

                return friends

            } catch (e: JSONException) {
                throw VKApiIllegalResponseException(e)
            }
        }

    }
}