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

val clientId = localProperties.getProperty("clientId")
val clientSecret = localProperties.getProperty("clientSecret")
val vkAppId = localProperties.getProperty("vkAppId")

vkidManifestPlaceholders {
    // Добавьте плейсхолдеры сокращенным способом. Например, vkidRedirectHost будет "vk.ru", а vkidRedirectScheme будет "vk$clientId".

    init(
        clientId = clientId,
        clientSecret = clientSecret,
    )
    // Или укажите значения явно через properties, если не хотите использовать плейсхолдеры.
    vkidRedirectHost = "vk.ru" // Обычно vk.ru.
    vkidRedirectScheme = "vk$vkAppId" // Строго в формате vk{ID приложения}.
    vkidClientId = clientId
    vkidClientSecret = clientSecret
}