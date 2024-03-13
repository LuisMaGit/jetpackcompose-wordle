plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = ProjectConfig.routerId
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    // internal modules
    implementation(project(Modules.game))
    implementation(project(Modules.core))

    // compose
    implementation(platform(Compose.billOfMaterials))
    implementation(Compose.navigation)

    // hilt
    implementation(Hilt.hiltAndroid)
    implementation(Hilt.hiltNavigation)
    kapt(Hilt.hiltCompiler)
}