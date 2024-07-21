plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

android {
    namespace = "com.example.close"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.close"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    secrets {
        // Optionally specify a different file name containing your secrets.
        // The plugin defaults to "local.properties"
        propertiesFileName = "secrets.properties"

        // A properties file containing default secret values. This file can be
        // checked in version control.
        defaultPropertiesFileName = "local.defaults.properties"

        // Configure which keys should be ignored by the plugin by providing regular expressions.
        // "sdk.dir" is ignored by default.
        ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
        ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
    }

}

dependencies {

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")  //auth library

    // Declare the dependency for the Cloud Firestore library
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.media3:media3-common:1.3.1")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.8")

    //
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // location
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.maps.android:maps-compose:4.4.1")


    //CometChat
    implementation ("com.cometchat:chat-sdk-android:4.0.7")

    //serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //retrofit
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    //okhttp
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    //gson
    implementation ("com.google.code.gson:gson:2.11.0")


    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}