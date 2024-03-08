import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.example.convention"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    compileOnly("com.android.tools.build:gradle:8.1.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    compileOnly("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.0-1.0.13")
}

gradlePlugin {
    plugins {
        register("AndroidApplication") {
            id = "VK_testovoe.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("AndroidLibrary") {
            id = "VK_testovoe.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("AndroidCompose"){
            id = "VK_testovoe.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
    }
}