package io.v47.tmdb.http.tck.tests

import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.tck.TckTest
import io.v47.tmdb.http.tck.TckTestResult

internal abstract class AbstractTckTest(private val baseUrl: String) : TckTest {
    protected val apiKey: String = (System.getProperty("tmdb.apiKey") ?: System.getenv("API_KEY")).let {
        if (it.isNullOrBlank())
            throw IllegalArgumentException(
                "Missing api key: You have to provide a valid TMDB API key " +
                        "that relates to a linked application. You can provide the key either as a system " +
                        "property called 'tmdb.apiKey' or as an environment variable called 'API_KEY'"
            )
        else
            it
    }

    override fun verify(httpClientFactory: HttpClientFactory): TckTestResult {
        val client = httpClientFactory.createHttpClient(baseUrl)
        return doVerify(client)
    }

    protected abstract fun doVerify(httpClient: HttpClient): TckTestResult
}
