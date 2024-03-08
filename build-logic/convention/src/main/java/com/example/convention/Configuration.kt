import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal const val IMPLEMENTATION = "implementation"

internal fun Project.configureKotlinAndroid(ext: CommonExtension<*, *, *, *, *>) {
    with(ext) {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}

internal fun Project.configureCompose(ext : CommonExtension<*, *, *, *, *>) {
    with(ext) {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
        }
    }

    dependencies {
        IMPLEMENTATION(platform(libs.androidx.compose.bom))
    }
}

internal fun Project.configureBuildTypes(ext: ApplicationExtension) {
    with(ext) {
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}

internal fun PluginManager.applyFromCatalog(provider: Provider<PluginDependency>) =
    apply(provider.get().pluginId)
