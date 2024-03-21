plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.demosdk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.demosdk"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.facebook.android:facebook-login:8.1.0")

    implementation ("com.appsflyer:af-android-sdk:6.13.0")
    implementation ("com.android.installreferrer:installreferrer:2.2")
}