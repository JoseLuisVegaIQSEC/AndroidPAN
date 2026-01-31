import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.dependency.guard)
}

var keystorePropertiesFile = rootProject.file("local-env.properties")
var prop = Properties()
prop.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "mx.com.iqsec.sdkpan"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    lint {
        warningsAsErrors = true

    }
    dependencyGuard {
        //Ayuda a detectar valid configurations
        //./gradlew dependencyGuard
        //configuration("releaseRuntimeClasspath")
        configuration("debugRuntimeClasspath")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            ndk {
                abiFilters.clear()
                abiFilters.add("armeabi-v7a")
                abiFilters.add("arm64-v8a")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField ("String", "DEFAULT_HOST", prop.getProperty("DEV_DEFAULT_HOST").toString())
            buildConfigField ("String", "API_KEY", prop.getProperty("DEV_API_KEY").toString())
            buildConfigField ("Float", "ONet", prop.getProperty("ONet").toString())
            buildConfigField ("Float", "PNet", prop.getProperty("PNet").toString())
            buildConfigField ("Float", "RNet", prop.getProperty("RNet").toString())
            buildConfigField ("int", "MinFace", prop.getProperty("MinFace").toString())
            buildConfigField ("int", "idx", "0")
            buildConfigField ("int", "TB_EYES", prop.getProperty("RD_TB_EYES").toString())
            buildConfigField ("Double", "R_Face_Min", prop.getProperty("RD_R_Face_Min").toString())
            buildConfigField ("Double", "R_Face_Max", prop.getProperty("RD_R_Face_Max").toString())
            buildConfigField ("Double", "R_Toma_Lejana_min", prop.getProperty("RD_R_Toma_Lejana_min").toString())
            buildConfigField ("Double", "R_Toma_Lejana_max", prop.getProperty("RD_R_Toma_Lejana_max").toString())
            buildConfigField ("int", "UI_FaceCapture", "0")//0 = UI Base 1 = UI Animated
        }

        release {
            isMinifyEnabled = true
            ndk {
                abiFilters.clear()
                abiFilters.add("armeabi-v7a")
                abiFilters.add("arm64-v8a")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField ("String", "DEFAULT_HOST", prop.getProperty("DEV_DEFAULT_HOST").toString())
            buildConfigField ("String", "API_KEY", prop.getProperty("DEV_API_KEY").toString())
            buildConfigField ("Float", "ONet", prop.getProperty("ONet").toString())
            buildConfigField ("Float", "PNet", prop.getProperty("PNet").toString())
            buildConfigField ("Float", "RNet", prop.getProperty("RNet").toString())
            buildConfigField ("int", "MinFace", prop.getProperty("MinFace").toString())
            buildConfigField ("int", "idx", "0")
            buildConfigField ("int", "TB_EYES", prop.getProperty("RD_TB_EYES").toString())
            buildConfigField ("Double", "R_Face_Min", prop.getProperty("RD_R_Face_Min").toString())
            buildConfigField ("Double", "R_Face_Max", prop.getProperty("RD_R_Face_Max").toString())
            buildConfigField ("Double", "R_Toma_Lejana_min", prop.getProperty("RD_R_Toma_Lejana_min").toString())
            buildConfigField ("Double", "R_Toma_Lejana_max", prop.getProperty("RD_R_Toma_Lejana_max").toString())
            buildConfigField ("int", "UI_FaceCapture", "0")//0 = UI Base 1 = UI Animated
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

apply(from = "$rootDir/sdkPan/DeployModule.gradle")

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)

    implementation(libs.com.airbnb.lottie)

    implementation(libs.google.gson)

    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.interceptor)
    implementation(libs.squareup.converter)

    implementation(libs.androidx.preference)
    implementation(libs.androidx.security)

    implementation(libs.com.huawei.hms.location)
    implementation(libs.iqsec.autodetectionine.ellave)
    implementation(libs.iqsec.antispoffing.ellave)
}