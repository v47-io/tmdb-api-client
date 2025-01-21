rootProject.name = "tmdb-api-client"

include(
    "core",
    "http-client-tck",
    "api",
    "standalone",
    "http-client-java11",
    "quarkus:runtime",
    "quarkus:deployment",
    "quarkus:integration-tests"
)
