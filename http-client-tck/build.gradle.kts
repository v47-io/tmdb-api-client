plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
}

dependencies {
    api(project(":core"))

    implementation(libs.mutiny)
}

description = "TCK for TMDb HttpClient implementations"
