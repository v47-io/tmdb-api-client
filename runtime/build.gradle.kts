plugins {
    id("tmdb-api-client.module")

    id("io.quarkus.extension")
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom))

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
