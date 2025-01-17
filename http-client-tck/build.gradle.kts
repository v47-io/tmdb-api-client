plugins {
    id("tmdb-api-client.module")
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
