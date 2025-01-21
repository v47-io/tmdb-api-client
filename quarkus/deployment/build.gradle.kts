import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.getKaptConfigurationName

plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
    id("tmdb-api-client.relocation")

    kotlin("kapt")
}

configurations.getByName(getKaptConfigurationName(sourceSets.main.name)) {
    extendsFrom(configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME))
}

configurations.getByName(getKaptConfigurationName(sourceSets.test.name)) {
    extendsFrom(configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME))
}

dependencies {
    implementation(platform(libs.quarkus.bom))

    implementation(project(":quarkus:runtime"))

    implementation(libs.quarkus.arc.deployment)
    implementation(libs.quarkus.jackson.deployment)
    implementation(libs.quarkus.kotlin.deployment)
    implementation(libs.quarkus.vertx.deployment)
}

publishing {
    publications {
        named<MavenPublication>("relocation") {
            artifactId = "quarkus-deployment"
        }
    }
}
