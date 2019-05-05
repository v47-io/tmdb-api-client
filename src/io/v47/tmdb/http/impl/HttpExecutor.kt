package io.v47.tmdb.http.impl

import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.ratelimiter.operator.RateLimiterOperator
import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpRequest
import org.reactivestreams.Publisher
import java.net.URLEncoder
import java.time.Duration

private const val BASE_URL = "https://api.themoviedb.org"
private val requestPathParamRegex = Regex("""\{(\w[\w_]*)}""", RegexOption.IGNORE_CASE)

class HttpExecutor(
    httpClientFactory: HttpClientFactory,
    private val apiKey: String,
    rateLimiterRegistry: RateLimiterRegistry? = null
) {
    private val httpClient = httpClientFactory.createHttpClient(BASE_URL)

    private val rateLimiterRegistry = rateLimiterRegistry ?: RateLimiterRegistry.ofDefaults()

    private val rateLimiter = this.rateLimiterRegistry.rateLimiter(
        "tmdb-api-v2",
        RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(10))
            .limitForPeriod(40)
            .build()
    )

    internal fun <T : Any> execute(request: TmdbRequest<T>): Publisher<T> {
        val httpRequest = createHttpRequest(request)

        @Suppress("UNCHECKED_CAST")
        return Flowable.fromPublisher(httpClient.execute<T>(httpRequest, request.responseType))
            .lift(RateLimiterOperator.of(rateLimiter))
            .filter { it.body != null }
            .map { it.body!! }
            .defaultIfEmpty(Unit as T)
    }

    private fun createHttpRequest(tmdbRequest: TmdbRequest<*>): HttpRequest {
        val url = buildRequestUrl(tmdbRequest.path, tmdbRequest.apiVersion, tmdbRequest.pathArgs)
        val query = tmdbRequest.queryArgs + ("api_key" to listOf(apiKey))

        return HttpRequestImpl(tmdbRequest.method, url, query, tmdbRequest.requestEntity)
    }

    private fun buildRequestUrl(
        path: String,
        apiVersion: Int,
        pathArgs: Map<String, Any>
    ): String {
        val filledInPath = path
            .trim()
            .split('/')
            .asSequence()
            .filter { !it.isBlank() }
            .map {
                if ('{' in it)
                    requestPathParamRegex.replace(it) { m ->
                        val name = m.groupValues[1]
                        if (name !in pathArgs)
                            throw IllegalArgumentException("Path argument missing: $name")
                        else
                            urlEncode(pathArgs.getValue(name))
                    }
                else
                    urlEncode(it)
            }
            .joinToString("/")

        return "/$apiVersion/$filledInPath"
    }

    private fun urlEncode(value: Any): String =
        when (value) {
            is String -> URLEncoder.encode(value, "utf-8")
            else -> URLEncoder.encode(value.toString(), "utf-8")
        }
}
