import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.getKaptConfigurationName

plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
    id("tmdb-api-client.relocation")

    kotlin("kapt")
    kotlin("plugin.allopen")

    id("io.quarkus.extension")
}

configurations.getByName(getKaptConfigurationName(sourceSets.main.name)) {
    extendsFrom(configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME))
}

configurations.getByName(getKaptConfigurationName(sourceSets.test.name)) {
    extendsFrom(configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME))
}

allOpen {
    annotation("jakarta.enterprise.context.ApplicationScoped")
}

dependencies {
    implementation(platform(libs.quarkus.bom))

    api(project(":api"))

    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.jackson)
    implementation(libs.quarkus.kotlin)
    implementation(libs.quarkus.vertx)

    implementation(libs.smallrye.mutiny.vertx.web.client)

    runtimeOnly(libs.jacksonModuleKotlin)
}

description = "Runtime module for the TMDB API client Quarkus extension"

publishing {
    publications {
        named<MavenPublication>("relocation") {
            artifactId = "quarkus"
        }
    }
}
