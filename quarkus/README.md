# TMDB API Client (Quarkus Extension)

This extension ensures TMDB API Client works as expected in Quarkus applications.

## Usage

Add the following dependency to your project:

```groovy
// Gradle
implementation 'io.v47.tmdb-api-client.quarkus:runtime:5.0.0'
```

```kotlin
// Gradle Kotlin DSL
implementation("io.v47.tmdb-api-client.quarkus:runtime:5.0.0")
```

```xml
<!--Maven-->
<dependencies>
    <dependency>
        <groupId>io.v47.tmdb-api-client.quarkus</groupId>
        <artifactId>runtime</artifactId>
        <version>5.0.0</version>
    </dependency>
</dependencies>
```

This will include the `runtime`, `deployment`, `api`, and `core` packages in your project,
so you can get started immediately.

Add the following configuration property to your `application.properties`:

```properties
tmdb-api-client.api-key=${API_KEY}
```

Then you can simply inject an instance of `TmdbClient` into your services and controllers:

```java
package myPackage;

import io.v47.tmdb.TmdbClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MyService {
    private final TmdbClient tmdbClient;

    @Inject
    public MyService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }
}
```

### Using a custom `TmdbApiKeyProvider`

By default, the extension will use its own implementation that uses the configuration property
mentioned above. If you want to control how the API Key is obtained, e.g. dynamically during
runtime, you can create your own `TmdbApiKeyProvider` implementation.

#### Example implementation

```java
package myPackage;

import io.quarkus.arc.Priority;
import io.v47.tmdb.api.key.TmdbApiKeyProvider;
import org.jetbrains.annotations.NotNull;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;

@Alternative
@Priority(1)
@ApplicationScoped
public class MyApiKeyProvider implements TmdbApiKeyProvider {
    @NotNull
    @Override
    public String getApiKey() {
        return "my-own-api-key";
    }
}
```

Quarkus will then use an instance of `MyApiKeyProvider` when creating the default `TmdbClient` bean.

## Features

This library makes it possible to use the `TMDB API Client` in Quarkus and also does all the
required legwork to make it native compatible, so you can safely use it in your native Quarkus
applications.

The Quarkus extension provides a default instance of `TmdbClient`, but if you want to create your
own, e.g. because you want to supply a different API-Key at runtime, you can simply inject an
instance of `HttpClientFactory` and create your own `TmdbClient` instance.
