plugins {
    id("tmdb-api-client.module")

    kotlin("plugin.spring")
}

dependencies {
    api(project(":api"))

    implementation(project(":core"))
    implementation(project(":http-client-spring-webflux"))

    implementation(libs.springBootStarter)
    implementation(libs.sbsWebflux)
    implementation(libs.jacksonDatabind)

    testImplementation(libs.sbsTest)
    testImplementation(libs.mutinyReactor)

    testRuntimeOnly(libs.jacksonModuleKotlin)
    testRuntimeOnly(libs.logback)
}

publishing {
    publications {
        named<MavenPublication>("tmdbApiClient") {
            pom {
                description = "Spring Boot starter for the TMDb API client and Webflux integration"
            }
        }
    }
}
