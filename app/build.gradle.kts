import com.android.build.gradle.internal.scope.ProjectInfo.Companion.getBaseName
import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = ProjectConfig.applicationId
    compileSdk = ProjectConfig.compileSdk

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    defaultConfig {
        applicationId = ProjectConfig.applicationId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = Kotlin.javaCompatibility
        targetCompatibility = Kotlin.javaCompatibility
    }
    kotlinOptions {
        jvmTarget = Kotlin.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // internal modules
    implementation(project(Modules.coreUI))
    implementation(project(Modules.core))
    implementation(project(Modules.game))
    implementation(project(Modules.router))

    // androidX
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycleRunTime)

    //kotlin immutable
    implementation(Kotlin.kotlinXImmutable)

    // compose
    implementation(platform(Compose.billOfMaterials))
    implementation(Compose.activityCompose)
    implementation(Compose.ui)
    implementation(Compose.uiGraphics)
    implementation(Compose.uiTooling)
    implementation(Compose.material)
    implementation(Compose.navigation)
    debugImplementation(Compose.uiToolingPreview)

    // hilt
    implementation(Hilt.hiltAndroid)
    implementation(Hilt.hiltNavigation)
    kapt(Hilt.hiltCompiler)

    // firebase
    implementation(platform(Firebase.billOfMaterials))
    implementation(Firebase.analytics)

    // ad mob
    implementation(AddMob.ads)

    // test
    testImplementation(Test.junit4)
    testImplementation(Test.mockkAgent)
    testImplementation(Test.mockkAndroid)
}