import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val compileSdkApi = libs.versions.compileSdk.get().toString().toInt()
val minSdkApi = libs.versions.minSdk.get().toString().toInt()
val targetSdkApi = libs.versions.targetSdk.get().toString().toInt()
val jvmTargetVersion = libs.versions.jvmTarget.get()
val jvmTargetEnum = JvmTarget.valueOf("JVM_$jvmTargetVersion")

kotlin {
    jvmToolchain(jvmTargetVersion.toInt())
    compilerOptions {
        jvmTarget.set(jvmTargetEnum)
    }
}

android {
    namespace = "com.project.momentum"
    compileSdk = compileSdkApi

    defaultConfig {
        applicationId = "com.project.momentum"
        minSdk = minSdkApi
        targetSdk = targetSdkApi
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(jvmTargetVersion)
        targetCompatibility = JavaVersion.toVersion(jvmTargetVersion)
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.bundles.lifecycle)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.bundles.camerax)

    implementation(libs.bundles.coil)
    implementation(libs.landscapist.coil)
    implementation(libs.bundles.accompanist)

    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.ktor.serialization.kotlinx.json)


    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
