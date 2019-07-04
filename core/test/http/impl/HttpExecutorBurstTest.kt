package io.v47.tmdb.http.impl

import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.StandaloneMnClientFactory
import io.v47.tmdb.model.TvShowDetails
import io.v47.tmdb.utils.TypeInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpExecutorBurstTest {
    private lateinit var apiKey: String

    @BeforeAll
    fun setup() {
        val tmp: String? = System.getProperty("tmdb.apiKey") ?: System.getenv("API_KEY")
        if (tmp.isNullOrBlank())
            throw IllegalArgumentException(
                "Missing api key: You have to provide a valid TMDB API key " +
                        "that relates to a linked application. You can provide the key either as a system " +
                        "property called 'tmdb.apiKey' or as an environment variable called 'API_KEY'"
            )

        apiKey = tmp
    }

    @Test
    fun `rate limiting happens without failing when bursting requests`() {
        // Agents of S.H.I.E.L.D.
        // https://api.themoviedb.org/3/tv/1403?api_key=<REDACTED>

        val httpExecutor = HttpExecutor(
            StandaloneMnClientFactory(),
            apiKey,
            null,
            TimeLimiterConfig.ofDefaults()
        )

        val request = TmdbRequest<TvShowDetails>(
            HttpMethod.Get,
            "/tv/1403",
            3,
            emptyMap(),
            null,
            TypeInfo.Simple(TvShowDetails::class.java)
        )

        runBlocking {
            coroutineScope {
                val asyncResults = (0 until 80).map {
                    async {
                        httpExecutor.execute(request).awaitFirst()
                    }
                }

                asyncResults.forEach { it.await() }
            }
        }
    }
}
