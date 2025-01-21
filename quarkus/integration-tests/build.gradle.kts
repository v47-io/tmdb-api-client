import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.getKaptConfigurationName

plugins {
    id("tmdb-api-client.module")
    kotlin("kapt")

    id("io.quarkus")
}

configurations.getByName(getKaptConfigurationName(sourceSets.main.name)) {
    extendsFrom(configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME))
}

configurations.getByName(getKaptConfigurationName(sourceSets.test.name)) {
    extendsFrom(configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME))
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom))

    implementation(project(":quarkus:runtime"))
    implementation(project(":http-client-tck"))

    implementation(libs.jacksonModuleKotlin)
    implementation(libs.quarkus.junit5)
    implementation(libs.quarkus.rest.jackson)
    implementation(libs.quarkus.rest.kotlin)
    implementation(libs.rest.assured)
}

afterEvaluate {
    tasks
        .asSequence()
        .filter { it.name.contains("dokka", ignoreCase = true) }
        .forEach { it.enabled = false }
}
