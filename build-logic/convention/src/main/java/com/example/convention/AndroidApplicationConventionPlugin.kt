import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                applyFromCatalog(libs.plugins.androidApplication)
                applyFromCatalog(libs.plugins.jetbrainsKotlinAndroid)
            }

            extensions.configure<ApplicationExtension> {
                namespace = "com.example.vk_testovoe"
                compileSdk = 34
                defaultConfig {
                    applicationId = "com.example.vk_testovoe"
                    minSdk = 24
                    targetSdk = 34
                    versionCode = 1
                    versionName = "1.0"

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }
                configureKotlinAndroid(this)
                configureBuildTypes(this)
            }
        }
    }
}