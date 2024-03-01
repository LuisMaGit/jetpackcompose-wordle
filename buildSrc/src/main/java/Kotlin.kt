import org.gradle.api.JavaVersion

object Kotlin {
    const val version = "1.8.10"
    const val jvmTarget = "18"
    val javaCompatibility = JavaVersion.VERSION_18
    const val kotlinXImmutable = "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7"
}