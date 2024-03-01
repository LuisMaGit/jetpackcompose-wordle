plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("app.cash.sqldelight") version Sql.sqlDelightVersion
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = ProjectConfig.coreId
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
}

dependencies {
    // androidX
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycleRunTime)

    // sql
    implementation(Sql.sqlDelightAndroidDriver)

    //hilt
    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltCompiler)

    // test
    testImplementation(Test.junit4)
    testImplementation(Test.mockkAgent)
    testImplementation(Test.mockkAndroid)
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.core.db")
        }
    }
}
