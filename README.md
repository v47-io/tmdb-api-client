# TMDb API Client (for the JVM)

![GitHub Workflow Status][workflow-shield]
[![Maven Central][maven-shield]][maven-central]
![GitHub][license-shield]

[workflow-shield]: https://img.shields.io/github/actions/workflow/status/v47-io/tmdb-api-client/build.yml?branch=main

[maven-shield]: https://img.shields.io/maven-central/v/io.v47.tmdb-api-client/api

[maven-central]: https://central.sonatype.com/namespace/io.v47.tmdb-api-client

[license-shield]: https://img.shields.io/github/license/v47-io/tmdb-api-client

## Usage

Add the following dependency to your project:

```groovy
// Gradle
implementation 'io.v47.tmdb-api-client:standalone:5.5.0'
```

```kotlin
// Gradle Kotlin DSL
implementation("io.v47.tmdb-api-client:standalone:5.5.0")
```

```xml
<!--Maven-->
<dependencies>
    <dependency>
        <groupId>io.v47.tmdb-api-client</groupId>
        <artifactId>standalone</artifactId>
        <version>5.5.0</version>
    </dependency>
</dependencies>
```

This will include the `api`, `core`, `standalone`, and `http-client-java11` packages in your
project, so you can get started immediately using the default HTTP client (framework agnostic).

You can create a standalone TMDb client like this:

```kotlin
// Kotlin
const val API_KEY = "..."

val tmdbClient = StandaloneTmdbClient(API_KEY)
```

```java
class MyClass {
    private static final String API_KEY = "...";

    TmdbClient tmdbClient = StandaloneTmdbClient.WithApiKey(API_KEY);
}
```

### Other HTTP clients

If you don't want to use the `standalone` module, just add the `api` module and the `http-client-*`
module of your choice to your project.

## Features

This library provides access to the entire read-only API (v3) of TheMovieDb, authenticated
using an API-Key, including images.

Various integrations with application frameworks are also available:

- [Quarkus](./quarkus/README.md)

Adding support for more frameworks is fairly trivial, only a small adapter needs to be
implemented for the actual HTTP client. To make development easier a TCK is provided to
verify compatibility of the HTTP client adapter with the actual TMDb client.

Even though this library is implemented using Kotlin it remains fully compatible with
Java and exposes a Java-friendly reactive API.

## Documentation

Automatically generated Kotlin docs are available [here](https://v47-io.github.io/tmdb-api-client/).

## Building

Requirements:

- JDK 21
- Docker (for Quarkus extension integration-tests)

For local building and testing you need to configure a TMDb API-Key using
the environment variable `API_KEY`.

The Java 11 HTTP client compatibility test uses a local HTTP server and does not require an API key;
tests that call TMDb and the full integration-test suite do. The Gradle wrapper is pinned to 9.7.0,
the upper supported Gradle version for Kotlin 2.4.21. Quarkus uses the maintained 3.40 LTS line,
and its BOM manages the Quarkus runtime dependencies without forced transitive overrides. The Kotlin
Gradle plugin uses 2.4.21 while the Kotlin standard library follows Quarkus BOM version 2.4.10.
Detekt uses `2.0.0-alpha.6` to avoid its Gradle 9 reporting API deprecation; this is a prerelease.

Then you can simply run a complete build of all packages using this command:

```shell
./gradlew build
```

To also run the Quarkus integration tests in native mode use this command:

```shell
./gradlew build -Dquarkus.package.jar.enabled=false -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```
