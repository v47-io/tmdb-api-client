plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
}

dependencies {
    compileOnly(libs.jacksonDatabind)

    testImplementation(project(":api"))
    testImplementation(libs.jacksonModuleKotlin)
}

publishing {
    publications {
        named<MavenPublication>("tmdbApiClient") {
            pom {
                description = "Core types and interfaces for the TMDB API client"
            }
        }
    }
}
