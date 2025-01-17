plugins {
    id("tmdb-api-client.module")

    kotlin("plugin.spring")
}

dependencies {
    compileOnly(project(":core"))

    compileOnly(libs.springContext)
    compileOnly(libs.springWebflux)
    implementation(libs.jacksonDatabind)
    runtimeOnly(libs.jacksonModuleKotlin)

    implementation(libs.mutiny)
    implementation(libs.mutinyReactor)

    testImplementation(project(":api"))
    testImplementation(project(":http-client-tck"))

    testImplementation(libs.sbsWebflux)
    testImplementation(libs.sbsTest)
}

publishing {
    publications {
        named<MavenPublication>("tmdbApiClient") {
            pom {
                description = "HttpClient implementation and integration for Spring Webflux"
            }
        }
    }
}
