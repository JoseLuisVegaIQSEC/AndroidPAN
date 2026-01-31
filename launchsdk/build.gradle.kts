import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.sonarqube)
}

var keystorePropertiesFile = rootProject.file("local-env.properties")
var prop = Properties()
prop.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "mx.com.iqsec.launchsdk"
    compileSdk = 36

    defaultConfig {
        applicationId = "mx.com.iqsec.launchsdk"
        minSdk = 24
        targetSdk = 36
        versionCode = 18
        versionName = "0.0.18"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            jniLibs {
                excludes.add("**/libTransform.so")
            }
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            ndk {
                abiFilters.clear()
                abiFilters.add("armeabi-v7a")
                abiFilters.add("arm64-v8a")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            ndk {
                abiFilters.clear()
                abiFilters.add("armeabi-v7a")
                abiFilters.add("arm64-v8a")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics.ktx)

    implementation(project(":sdkPan"))
//    implementation(libs.iqsec.sdkpan.onboarding)
}