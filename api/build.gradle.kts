plugins {
    id("tmdb-api-client.module")

    kotlin("plugin.allopen")
}

allOpen {
    annotation("io.v47.tmdb.utils.OpenTmdbClient")
}

dependencies {
    api(project(":core"))

    api(libs.nvI18n)

    implementation(libs.jacksonDatabind)
    implementation(libs.mutiny)

    runtimeOnly(libs.jacksonModuleKotlin)

    testImplementation(project(":http-client-java11"))

    testRuntimeOnly(libs.logback)
}

publishing {
    publications {
        named<MavenPublication>("tmdbApiClient") {
            pom {
                description.set("The primary API of the TMDB API client")
            }
        }
    }
}
