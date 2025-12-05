plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    // 1. ADD A NAMESPACE
    // This is required and should be unique for your application.
    namespace = "com.example.smartdialer_mvp" // You can change 'com.example' to your own domain

    // 2. SET THE COMPILE SDK VERSION
    // This should generally be the latest stable API level.
    compileSdk = 34

    defaultConfig {
        // 3. DEFINE THE APPLICATION ID
        // This uniquely identifies your app on a device and in the Play Store.
        applicationId = "com.example.smartdialer_mvp" // Match your namespace or make it unique
        minSdk = 24 // The minimum Android version your app will support
        targetSdk = 34 // The Android version your app is tested against
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    // Your existing compatibility settings are correct
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // Add this block if you are using Jetpack Compose
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Make sure this version is compatible with your Kotlin version
    }
}

// This dependencies block is necessary to include your libraries
dependencies {
    implementation("androidx.core:core-ktx:1.12.0") // Updated to a more recent version
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // Updated
    implementation("androidx.activity:activity-compose:1.8.2") // Updated
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material") // Your existing material dependency
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui:1.6.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
    implementation("androidx.compose.ui:ui:1.6.1")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.5")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Room dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // annotationProcessor("androidx.room:room-compiler:2.6.1") // You will likely need this for Room

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}
