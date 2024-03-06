plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = ProjectConfig.applicationId
    compileSdk = ProjectConfig.compileSdk

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
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    // androidX
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycleRunTime)

    //kotlin immutable
    implementation(Kotlin.kotlinXImmutable)

    // compose
    implementation(platform(Compose.bom))
    implementation(Compose.activityCompose)
    implementation(Compose.ui)
    implementation(Compose.uiGraphics)
    implementation(Compose.uiTooling)
    implementation(Compose.material)
    debugImplementation(Compose.uiToolingPreview)

    // hilt
    implementation(Hilt.hiltAndroid)
    implementation(Hilt.hiltNavigation)
    kapt(Hilt.hiltCompiler)

    // test
    testImplementation(Test.junit4)
    testImplementation(Test.mockkAgent)
    testImplementation(Test.mockkAndroid)
}