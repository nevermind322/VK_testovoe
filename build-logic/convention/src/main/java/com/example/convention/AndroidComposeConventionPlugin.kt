import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.builder.model.AndroidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val library = extensions.findByType(LibraryExtension::class)
            if (library != null)
                configureCompose(library)
            val app = extensions.findByType(ApplicationExtension::class)
            if (app != null)
                configureCompose(app)
        }
    }
}