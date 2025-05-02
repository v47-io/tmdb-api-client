plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
}

dependencies {
    compileOnly(libs.jacksonDatabind)

    testImplementation(project(":api"))
    testImplementation(libs.jacksonModuleKotlin)
}

mavenPublishing {
    pom {
        description = "Core types and interfaces for the TMDB API client"
    }
}
