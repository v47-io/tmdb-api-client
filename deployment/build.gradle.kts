plugins {
    id("tmdb-api-client.module")
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom))

    implementation(project(":runtime"))

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
