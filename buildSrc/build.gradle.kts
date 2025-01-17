import org.gradle.accessors.dm.LibrariesForLibs
import java.util.*

private val Project.libs: LibrariesForLibs
    get() = extensions.getByType()

plugins {
    `kotlin-dsl`

    alias(libs.plugins.license)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugin.detekt)
    implementation(libs.plugin.dokka)
    implementation(libs.plugin.git.properties)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.kotlin.allopen)
    implementation(libs.plugin.kotlin.spring)
    implementation(libs.plugin.license)
    implementation(libs.plugin.release)
    implementation(libs.remalGradlePlugins)
}

license {
    excludes(
        setOf(
            "**/*.json",
            "**/*.properties",
            "**/META-INF/**/*"
        )
    )

    mapping("kts", "SLASHSTAR_STYLE")

    header = rootProject.file("../LICENSE")
    skipExistingHeaders = true

    ext {
        set("year", Calendar.getInstance().get(Calendar.YEAR))
    }
}
