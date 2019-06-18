package io.v47.tmdb.http.impl

import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.ratelimiter.operator.RateLimiterOperator
import io.github.resilience4j.timelimiter.TimeLimiter
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.ErrorResponseException
import org.reactivestreams.Publisher
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean

private const val BASE_URL = "https://api.themoviedb.org"

@Suppress("MagicNumber")
class HttpExecutor(
    private val httpClientFactory: HttpClientFactory,
    private val apiKey: String,
    rateLimiterRegistry: RateLimiterRegistry? = null,
    timeLimiterConfig: TimeLimiterConfig
) {
    private lateinit var httpClient: HttpClient
    private var httpClientInitialized = AtomicBoolean(false)

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
    fun <T : Any> execute(request: TmdbRequest<T>): Publisher<T> {
        val httpRequest = createHttpRequest(request)

        if (!httpClientInitialized.getAndSet(true))
            httpClient = httpClientFactory.createHttpClient(BASE_URL)

        return Flowable
            .fromPublisher(
                timeLimiter.executeFutureSupplier {
                    CompletableFuture.supplyAsync {
                        httpClient.execute(
                            httpRequest,
                            request.responseType
                        )
                    }
                }
            )
            .subscribeOn(Schedulers.io())
            .lift(RateLimiterOperator.of(rateLimiter))
            .filter { it.body != null }
            .map { resp ->
                when {
                    resp.status == 200 -> resp.body
                    resp.body is ErrorResponse -> throw ErrorResponseException(
                        resp.body as ErrorResponse,
                        httpRequest
                    )
                    else -> throw IllegalArgumentException("Invalid error response: $resp")
                }
            }
            .map { it as T }
            .defaultIfEmpty(Unit as T)
    }

    private fun createHttpRequest(tmdbRequest: TmdbRequest<*>): HttpRequest {
        val url = "/${tmdbRequest.apiVersion}/${tmdbRequest.path.trim(' ', '/')}"
        val query = tmdbRequest.queryArgs + ("api_key" to listOf(apiKey))

        return HttpRequestImpl(tmdbRequest.method, url, query, tmdbRequest.requestEntity)
    }
}
