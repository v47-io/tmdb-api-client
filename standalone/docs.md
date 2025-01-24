# Module standalone

Provides a straightforward way to create a [TmdbClient][io.v47.tmdb.TmdbClient] instance
that uses the [HttpClient][io.v47.tmdb.http.HttpClient] implementation from the `http-client-java11`
module.

This makes it independent of any framework like Quarkus.

To create a `TmdbClient` simply use the factory method (Java) or invoke operator (Kotlin):

```java
import io.v47.tmdb.StandaloneTmdbClient;

public class Java {
    public static final TmdbClient tmdbClient = StandaloneTmdbClient.WithApiKey("your api key");
}
```

```kotlin
import io.v47.tmdb.StandaloneTmdbClient

val tmdbClient = StandaloneTmdbClient("your api key")
```
