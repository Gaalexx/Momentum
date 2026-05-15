// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

val localProperties = java.util.Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

fun localProperty(name: String): String? =
    localProperties.getProperty(name)?.trim()?.removeSurrounding("\"")?.takeIf { it.isNotEmpty() }

fun requiredLocalProperty(name: String): String =
    localProperty(name)
        ?: error("$name not found in local.properties")

val vkAppId = requiredLocalProperty("vkAppId")
val vkClientId = requiredLocalProperty("clientId")
val vkClientSecret = requiredLocalProperty("clientSecret")

vkidManifestPlaceholders {
    init(clientId = vkClientId, clientSecret = vkClientSecret)
    vkidRedirectHost = "vk.ru"
    vkidRedirectScheme = "vk$vkAppId"
    vkidClientId = vkClientId
    vkidClientSecret = vkClientSecret
}
