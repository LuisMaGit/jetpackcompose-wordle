plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = ProjectConfig.gameId
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(Modules.coreUI))
    implementation(project(Modules.core))

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
    kapt(Hilt.hiltCompiler)

    // test
    testImplementation(Test.junit4)
    testImplementation(Test.mockkAgent)
    testImplementation(Test.mockkAndroid)
}