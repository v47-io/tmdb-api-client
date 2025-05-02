plugins {
    id("tmdb-api-client.module")
    id("tmdb-api-client.publication")

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
    implementation(libs.slf4j)

    runtimeOnly(libs.jacksonModuleKotlin)

    testImplementation(project(":http-client-java11"))

    testRuntimeOnly(libs.logback)
}

mavenPublishing {
    pom {
        description = "The primary API of the TMDB API client"
    }
}
