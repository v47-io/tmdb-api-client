plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")
}

dependencies {
    implementation(libs.jacksonDatabind)
    runtimeOnly(libs.jacksonModuleJsr310)
    runtimeOnly(libs.jacksonModuleKotlin)

    implementation(libs.mutiny)

    compileOnly(project(":core"))

    testImplementation(project(":api"))
    testImplementation(project(":http-client-tck"))
}

publishing {
    publications {
        named<MavenPublication>("tmdbApiClient") {
            pom {
                description = "TMDb HttpClient implementation using the Java 11 HttpClient"
            }
        }
    }
}
