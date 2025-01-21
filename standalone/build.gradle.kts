plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
}

dependencies {
    api(project(":api"))
    api(project(":core"))

    implementation(project(":http-client-java11"))
}

description = "Standalone TMDb API client package for framework-less access to TMDb"
