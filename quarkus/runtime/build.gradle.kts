import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.getKaptConfigurationName

plugins {
    id("tmdb-api-client.module")

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

    api(libs.tmdb.api)

    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.jackson)
    implementation(libs.quarkus.kotlin)
    implementation(libs.quarkus.vertx)

    implementation(libs.smallrye.mutiny.vertx.web.client)

    runtimeOnly(libs.jacksonModuleKotlin)
}

publishing {
    publications {
        named<MavenPublication>("relocation") {
            artifactId = "quarkus"
        }
    }
}
