package io.v47.tmdb.http.impl

import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.ratelimiter.operator.RateLimiterOperator
import io.github.resilience4j.timelimiter.TimeLimiter
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpRequest
import org.reactivestreams.Publisher
import java.time.Duration
import java.util.concurrent.CompletableFuture

private const val BASE_URL = "https://api.themoviedb.org"

@Suppress("MagicNumber")
class HttpExecutor(
    httpClientFactory: HttpClientFactory,
    private val apiKey: String,
    rateLimiterRegistry: RateLimiterRegistry? = null,
    timeLimiterConfig: TimeLimiterConfig
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

    private val timeLimiter = TimeLimiter.of(timeLimiterConfig)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> execute(request: TmdbRequest<T>): Publisher<T> =
        Flowable
            .fromPublisher(
                timeLimiter.executeFutureSupplier {
                    CompletableFuture.supplyAsync {
                        httpClient.execute<T>(
                            createHttpRequest(request),
                            request.responseType
                        )
                    }
                }
            )
            .lift(RateLimiterOperator.of(rateLimiter))
            .filter { it.body != null }
            .map { it.body!! }
            .defaultIfEmpty(Unit as T)

    private fun createHttpRequest(tmdbRequest: TmdbRequest<*>): HttpRequest {
        val url = "/${tmdbRequest.apiVersion}/${tmdbRequest.path.trim(' ', '/')}"
        val query = tmdbRequest.queryArgs + ("api_key" to listOf(apiKey))

        return HttpRequestImpl(tmdbRequest.method, url, query, tmdbRequest.requestEntity)
    }
}
