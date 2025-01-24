plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
}

dependencies {
    api(project(":core"))

    implementation(libs.mutiny)
}

publishing {
    publications {
        named<MavenPublication>("tmdbApiClient") {
            pom {
                description = "TCK for TMDb HttpClient implementations"
            }
        }
    }
}
