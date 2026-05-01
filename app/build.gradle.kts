import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.properties.Properties
import shadow.bundletool.com.android.ddmlib.Log
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("com.google.gms.google-services")
}

val compileSdkApi = libs.versions.compileSdk.get().toInt()
val minSdkApi = libs.versions.minSdk.get().toInt()
val targetSdkApi = libs.versions.targetSdk.get().toInt()
val jvmTargetVersion = libs.versions.jvmTarget.get()
val jvmTargetEnum = JvmTarget.valueOf("JVM_$jvmTargetVersion")
val javaVersion = JavaVersion.toVersion(jvmTargetVersion)


val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

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

//        buildConfigField(
//            "String",
//            "BACKEND_BUILD_URL",
//            "\"http://192.168.1.122/api/momentum/\""
//        )
        buildConfigField(
            "String",
            "BACKEND_BUILD_URL",
            "\"http://193.233.20.47/api/momentum/\""
        )

        buildConfigField(
            "String",
            "EMAIL_CHECKER",
            "\"https://rapid-email-verifier.fly.dev/api/\""
        )

        buildConfigField(
            "String",
            "API_KEY",
            localProperties.getProperty("API_KEY") ?: run {
                Log.w("Momentum", "API_KEY not found in local.properties")
                "\"\""
            }
        )
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
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.datastore.preferences)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.compose.constraintlayout)

    // Material
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)

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
    implementation(libs.ktor.client.android.v341)

    // Dependency injection
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.ui.text)
    implementation(libs.androidx.foundation)

    ksp(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.androidx.test)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    //gms
    implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
}
