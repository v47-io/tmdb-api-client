# Client for The Movie Database (version 2)

## Usage

Add the following dependency to your project:

```groovy
// Gradle
implementation 'io.v47.tmdb-api-client:standalone:3.0.0-SNAPSHOT'
```

```kotlin
// Gradle Kotlin DSL
implementation("io.v47.tmdb-api-client:standalone:3.0.0-SNAPSHOT")
```

```xml
<!--Maven-->
<dependencies>
    <dependency>
        <groupId>io.v47.tmdb-api-client</groupId>
        <artifactId>standalone</artifactId>
        <version>3.0.0-SNAPSHOT</version>
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

This library provides access to the entire read-only API of TheMovieDb, authenticated
using an API-Key, including pictures.

Various integrations with application frameworks are also available:

- Micronaut
- Spring Boot Webflux (Starter provided)

Adding support for more frameworks is fairly trivial, only a small adapter needs to be
implemented for the actual HTTP client. To make development easier a TCK is provided to
verify compatibility of the HTTP client adapter with the actual TMDb client.

Even though this library is implemented using Kotlin it remains fully compatible with
Java and exposes a Java-friendly reactive API.

## Documentation

Automatically generated Kotlin docs are available [here](https://v47-io.github.io/tmdb-api-client/).

## Building

Requirements:

- JDK 11

For local building and testing you need to configure a TMDb API-Key using
the environment variable `API_KEY`.

Then you can simply run a complete build of all packages using this command:

```shell
./gradlew build
```