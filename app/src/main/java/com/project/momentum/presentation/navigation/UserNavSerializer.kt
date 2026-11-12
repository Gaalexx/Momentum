package com.project.momentum.presentation.navigation

import com.project.momentum.domain.friends.model.User
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Navigation owns persistence; the domain User has no serialization dependency. */
object UserNavSerializer : KSerializer<User> {
    override val descriptor: SerialDescriptor = UserNavArgs.serializer().descriptor

    override fun serialize(encoder: Encoder, value: User) {
        encoder.encodeSerializableValue(
            UserNavArgs.serializer(),
            UserNavArgs(value.id, value.name, value.email, value.phoneNumber, value.avatarUrl,
                value.isOnline, value.description, value.hasPremium)
        )
    }

    override fun deserialize(decoder: Decoder): User =
        decoder.decodeSerializableValue(UserNavArgs.serializer()).let {
            User(it.id, it.name, it.email, it.phoneNumber, it.avatarUrl, it.isOnline,
                it.description, it.hasPremium)
        }
}

@Serializable
private data class UserNavArgs(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val description: String? = null,
    val hasPremium: Boolean = false
)
