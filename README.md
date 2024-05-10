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
implementation 'io.v47.tmdb-api-client:standalone:4.7.0'
```

```kotlin
// Gradle Kotlin DSL
implementation("io.v47.tmdb-api-client:standalone:4.7.0")
```

```xml
<!--Maven-->
<dependencies>
    <dependency>
        <groupId>io.v47.tmdb-api-client</groupId>
        <artifactId>standalone</artifactId>
        <version>4.7.0</version>
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

Various integrations with application frameworks are also available (`artifactId`):

- Spring Boot Webflux (`spring-boot-starter`)
- [Quarkus][tmdb-api-client-quarkus] (`quarkus`)

__All integrations except the Java 11 HttpClient based implementation (also standalone) and the Quarkus 
client are deprecated and won't get any major updates except critical fixes ensuring they are secure 
and test ok.__

[tmdb-api-client-quarkus]: https://github.com/v47-io/tmdb-api-client-quarkus-ext

Adding support for more frameworks is fairly trivial, only a small adapter needs to be
implemented for the actual HTTP client. To make development easier a TCK is provided to
verify compatibility of the HTTP client adapter with the actual TMDb client.

Even though this library is implemented using Kotlin it remains fully compatible with
Java and exposes a Java-friendly reactive API.

## Documentation

Automatically generated Kotlin docs are available [here](https://v47-io.github.io/tmdb-api-client/).

## Building

Requirements:

- JDK 17

For local building and testing you need to configure a TMDb API-Key using
the environment variable `API_KEY`.

Then you can simply run a complete build of all packages using this command:

```shell
./gradlew build
```
