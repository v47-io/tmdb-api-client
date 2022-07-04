# Module http-client-tck

Provides a standardized way of verifying the expected behavior of
an [HttpClient][io.v47.tmdb.http.HttpClient] implementation.

To use the TCK simply create a test that calls
the [verify][io.v47.tmdb.http.tck.HttpClientTck.verify] function:

```kotlin
package myhttpclient

import io.v47.tmdb.http.tck.HttpClientTck
import io.v47.tmdb.http.tck.TckResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MyHttpClientTckTest {
    @Test
    fun executeTckTest() {
        val result = HttpClientTck().verify(MyAwesomeHttpClientFactoryImpl())

        if (result is TckResult.Failure)
            result.failedTests.forEach { failedTest ->
                Assertions.assertEquals(
                    failedTest.expectedValue,
                    failedTest.actualValue,
                    "Test ${failedTest.name} failed"
                )
            }
    }
}
```

Please keep in mind you need to provide a valid TMDb API key using the system property `tmdb.apiKey`
or the environment variable `API_KEY`.

# Package io.v47.tmdb.http.tck

Contains the public API of the TCK
