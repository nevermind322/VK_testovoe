import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                applyFromCatalog(libs.plugins.androidLibrary)
                applyFromCatalog(libs.plugins.jetbrainsKotlinAndroid)
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
        }
    }
}