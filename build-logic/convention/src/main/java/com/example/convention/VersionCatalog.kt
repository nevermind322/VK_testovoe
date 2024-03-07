import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

internal val Project.libs
    get() = the<org.gradle.accessors.dm.LibrariesForLibs>()


