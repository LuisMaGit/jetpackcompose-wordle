// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version Build.androidGradlePlugin apply false
    id("org.jetbrains.kotlin.android") version Kotlin.version apply false
    id("com.android.library") version Build.androidGradlePlugin apply false
    id("org.jetbrains.kotlin.jvm") version Kotlin.version apply false
    id("com.google.dagger.hilt.android") version Hilt.version apply false
    id("com.google.gms.google-services") version Firebase.googleServiceVersion apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}