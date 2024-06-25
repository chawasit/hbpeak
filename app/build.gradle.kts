plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-android")
//    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "coffee.hh.hbpeak"
    compileSdk = 34

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        applicationId = "coffee.hh.hbpeak"
        minSdk = 25
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation(libs.androidx.material3)
    // or skip Material Design and build directly on top of foundational components
    implementation(libs.androidx.foundation)
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation(libs.androidx.ui)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation(libs.androidx.material.icons.core)
    // Optional - Add full set of material icons
    implementation(libs.androidx.material.icons.extended)
    // Optional - Add window size utils

    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation(libs.androidx.runtime.livedata)
    // Optional - Integration with RxJava
    implementation(libs.androidx.runtime.rxjava2)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.auth)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.kotlinx.coroutines.core)

    // For WebSocket
    implementation(libs.java.websocket)

    // For serial communication
    //noinspection UseTomlInstead
    implementation("com.github.mik3y:usb-serial-for-android:3.7.3")


    implementation (libs.ktor.server.core)
    implementation (libs.ktor.server.netty)
    implementation (libs.ktor.server.websockets)
    implementation (libs.ktor.client.serialization)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m2)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.core)
    implementation(libs.vico.views)

    implementation(libs.mpandroidchart)
}

kapt {
    correctErrorTypes = true
}