package io.v47.tmdb.resilience

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.resilience4j.cache.Cache
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.circuitbreaker.operator.CircuitBreakerOperator
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.ratelimiter.operator.RateLimiterOperator
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.transformer.RetryTransformer
import io.github.resilience4j.timelimiter.TimeLimiter
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.reactivex.Flowable
import io.v47.tmdb.cache.PackedCache
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.utils.jackson.JacksonDecoder
import io.v47.tmdb.utils.unpack
import io.v47.tmdb.utils.vavr.invoke
import io.vavr.CheckedFunction0
import org.reactivestreams.Publisher
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

private val emptyByteArray = ByteArray(0)

internal class TmdbClientResilience(
    private val httpClient: HttpClient,
    objectMapper: ObjectMapper,
    circuitBreakerRegistry: CircuitBreakerRegistry?,
    rateLimiterRegistry: RateLimiterRegistry?,
    private val retryConfig: RetryConfig?,
    cache: Cache<String, ByteArray>?,
    timeLimiterConfig: TimeLimiterConfig?
) {
    private val jacksonDecoder = JacksonDecoder(objectMapper)

    private val circuitBreakerRegistry =
        circuitBreakerRegistry ?: CircuitBreakerRegistry.ofDefaults()

    @Suppress("MagicNumber")
    private val rateLimiterRegistry =
        rateLimiterRegistry ?: RateLimiterRegistry.of(
            RateLimiterConfig.custom()
                .limitForPeriod(4)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .build()
        )

    private val cache = cache?.let { PackedCache(it) }

    @Suppress("MagicNumber")
    private val timeLimiter =
        timeLimiterConfig?.let { TimeLimiter.of(it) }
            ?: TimeLimiter.of(Duration.ofSeconds(3))

    fun decorate(httpRequest: HttpRequest): CheckedFunction0<Publisher<HttpResponse>> =
        CheckedFunction0 {
            var usingCachedResult = false
            val httpResponsesToCache = mutableListOf<HttpResponse>()

            Flowable
                .fromCallable(
                    TimeLimiter.decorateFutureSupplier(
                        timeLimiter,
                        Supplier {
                            CompletableFuture.supplyAsync {
                                httpClient.execute(httpRequest)
                            }
                        }
                    )
                )
                .flatMap { Flowable.fromPublisher(it) }
                .lift(RateLimiterOperator.of(rateLimiterRegistry.rateLimiter("tmdb-api")))
                .lift(CircuitBreakerOperator.of(circuitBreakerRegistry.circuitBreaker("tmdb-api")))
                .compose(RetryTransformer.of(createRetry("tmdb-api")))
                .onErrorResumeNext { throwable: Throwable ->
                    runCatching { cache?.computeIfAbsent(httpRequest, noSuchElement()) }
                        .recoverCatching { if (it is NoSuchElementException) null else throw it }
                        .mapCatching { it ?: throw NoSuchElementException() }
                        .fold(
                            { ba ->
                                usingCachedResult = true
                                Flowable.fromIterable(unpack<Iterable<CachedHttpResponse>>(ba))
                            },
                            { x ->
                                throwable.addSuppressed(x)
                                Flowable.error(throwable)
                            }
                        )
                }
                .doOnNext { next ->
                    if (cache != null && !usingCachedResult)
                        httpResponsesToCache += next
                }
                .doOnComplete {
                    if (!usingCachedResult)
                        cache?.computeIfAbsent(httpRequest, CheckedFunction0 { httpResponsesToCache as Any })
                }
        }

    fun <R : Any> decorate(
        httpRequest: HttpRequest,
        resultType: TypeReference<R>
    ): CheckedFunction0<Publisher<Pair<HttpResponse, R?>>> =
        CheckedFunction0 {
            val raw = decorate(httpRequest)()
            var firstResponse: HttpResponse? = null

            val flowable = Flowable.fromPublisher(raw)
                .doOnNext {
                    if (firstResponse == null)
                        firstResponse = it
                }
                .map { it.body ?: emptyByteArray }

            Flowable.fromPublisher(
                jacksonDecoder.decode(
                    flowable,
                    resultType
                )
            )
                .map { firstResponse!! to it as R? }
                .switchIfEmpty(Flowable.fromCallable { firstResponse!! to null })
        }

    private fun createRetry(name: String) =
        retryConfig?.let { Retry.of(name, it) }
            ?: Retry.ofDefaults(name)

    private fun noSuchElement(): CheckedFunction0<Any> = CheckedFunction0 { throw NoSuchElementException() }

    private data class CachedHttpResponse(
        override val status: Int,
        override val headers: Map<String, List<String>>,
        override val body: ByteArray?
    ) : HttpResponse {
        @Suppress("ComplexMethod")
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is HttpResponse) return false

            if (status != other.status) return false
            if (headers != other.headers) return false
            if (body != null) {
                if (other.body == null) return false
                if (!body.contentEquals(other.body!!)) return false
            } else if (other.body != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = status
            result = 31 * result + headers.hashCode()
            result = 31 * result + (body?.contentHashCode() ?: 0)
            return result
        }
    }
}
