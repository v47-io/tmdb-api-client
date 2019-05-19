package io.v47.tmdb.api

import io.v47.tmdb.TmdbClient
import io.v47.tmdb.http.TestMicronautClientFactory
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractTmdbTest {
    protected lateinit var client: TmdbClient

    @BeforeAll
    fun setup() {
        val apiKey = System.getProperty("tmdb.apiKey") ?: System.getenv("API_KEY")
        if (apiKey.isNullOrBlank())
            throw IllegalArgumentException(
                "Missing api key: You have to provide a valid TMDB API key " +
                        "that relates to a linked application. You can provide the key either as a system " +
                        "property called 'tmdb.apiKey' or as an environment variable called 'API_KEY'"
            )

        client = TmdbClient.blockingCreate(TestMicronautClientFactory(), apiKey)
    }
}
