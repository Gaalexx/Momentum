package com.project.momentum.data.auth.keystore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.serialization.Serializable
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class KeystoreManager @Inject constructor() {

    private val provider = "AndroidKeyStore"
    private val alias = "momentum_refresh_token_key"
    private val transformation = "AES/GCM/NoPadding"

    private fun getKeyStore(): KeyStore {
        return KeyStore.getInstance(provider).apply { load(null) }
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = getKeyStore()
        val existingKey = keyStore.getKey(alias, null) as? SecretKey
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            provider
        )

        val spec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setKeySize(256)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    fun encrypt(plainText: String): EncryptedData {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())

        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val iv = cipher.iv

        return EncryptedData(
            cipherText = cipherText,
            iv = iv
        )
    }

    fun decrypt(encryptedData: EncryptedData): String {
        val cipher = Cipher.getInstance(transformation)
        val spec = GCMParameterSpec(128, encryptedData.iv)

        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), spec)
        val plainBytes = cipher.doFinal(encryptedData.cipherText)

        return plainBytes.toString(Charsets.UTF_8)
    }
}

@Serializable
data class EncryptedData(
    val cipherText: ByteArray,
    val iv: ByteArray
)