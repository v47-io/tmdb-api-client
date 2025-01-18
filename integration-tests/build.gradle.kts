plugins {
    id("tmdb-api-client.module")

    id("io.quarkus")
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom))

    implementation(project(":runtime"))

    implementation(libs.jacksonModuleKotlin)
    implementation(libs.quarkus.junit5)
    implementation(libs.quarkus.rest.jackson)
    implementation(libs.rest.assured)
    implementation(libs.tmdb.tck)
}

publishing {
    publications {
        removeIf { true }
    }
}
