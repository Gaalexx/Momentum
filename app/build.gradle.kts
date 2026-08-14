import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("com.google.gms.google-services")
    id("vkid.manifest.placeholders")
}

val compileSdkApi = libs.versions.compileSdk.get().toInt()
val minSdkApi = libs.versions.minSdk.get().toInt()
val targetSdkApi = libs.versions.targetSdk.get().toInt()
val jvmTargetVersion = libs.versions.jvmTarget.get()
val jvmTargetEnum = JvmTarget.valueOf("JVM_$jvmTargetVersion")
val javaVersion = JavaVersion.toVersion(jvmTargetVersion)


val localProperties = Properties()
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

// Сначала переменная окружения (CI), потом local.properties (локальная сборка)
fun secret(name: String): String? =
    System.getenv(name)?.takeIf { it.isNotEmpty() } ?: localProperty(name)

val vkAppId = requiredLocalProperty("vkAppId")
val vkClientId = requiredLocalProperty("clientId")
val vkClientSecret = requiredLocalProperty("clientSecret")

require(vkAppId.all(Char::isDigit)) {
    "vkAppId must be numeric"
}

require(vkClientId == vkAppId) {
    "clientId and vkAppId in local.properties must match for VK SDK auth"
}

kotlin {
    jvmToolchain(jvmTargetVersion.toInt())
    compilerOptions {
        jvmTarget.set(jvmTargetEnum)
    }
}

android {
    namespace = "com.project.momentum"
    compileSdkVersion(compileSdkApi)

    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = secret("KEYSTORE_PASSWORD")
            keyAlias = secret("KEY_ALIAS")
            keyPassword = secret("KEY_PASSWORD")
        }
    }

    defaultConfig {
        applicationId = "com.project.momentum"
        minSdk = minSdkApi
        targetSdk = targetSdkApi
        // На CI берётся номер запуска, локально всегда 1
        versionCode = (System.getenv("GITHUB_RUN_NUMBER") ?: "1").toInt()
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "BACKEND_BUILD_URL",
            "\"http://193.233.20.47/api/momentum/\""
        )

//        buildConfigField(
//            "String",
//            "BACKEND_BUILD_URL",
//            "\"http://192.168.1.122/api/momentum/\""
//        )

        buildConfigField(
            "String",
            "EMAIL_CHECKER",
            "\"https://rapid-email-verifier.fly.dev/api/\""
        )

        buildConfigField(
            "String",
            "API_KEY",
            localProperties.getProperty("API_KEY") ?: run {
                logger.warn("API_KEY not found in local.properties")
                "\"\""
            }
        )

        resValue("integer", "com_vk_sdk_AppId", vkAppId)
        addManifestPlaceholders(
            mapOf(
                "VKIDRedirectHost" to "vk.ru",
                "VKIDRedirectScheme" to "vk$vkAppId",
                "VKIDClientID" to vkClientId,
                "VKIDClientSecret" to vkClientSecret,
            )
        )
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.datastore.preferences)

    // Compose BOM — обязателен на всех трёх конфигурациях,
    // иначе consistent resolution в AGP 9 роняет androidTest
    implementation(platform(libs.androidx.compose.bom))
    testImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.compose.constraintlayout)

    // Architecture
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation)

    // Media
    implementation(libs.bundles.camerax)
    implementation(libs.coil.compose)
    implementation(libs.coil.video)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)
    implementation(libs.media3.compose)

    // Networking
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor)
    implementation(libs.ktor.client.logging)

    // Dependency injection
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences.core)

    ksp(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit4)
    androidTestImplementation(libs.bundles.androidx.test)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // gms
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    // VK SDK
    implementation(libs.android.sdk.core)
    implementation(libs.android.sdk.api)
    implementation(libs.vkid)
}