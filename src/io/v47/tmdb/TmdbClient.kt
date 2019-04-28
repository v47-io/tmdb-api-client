package io.v47.tmdb

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.resilience4j.cache.Cache
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.resilience.TmdbClientResilience

class TmdbClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper,
    private val apiKey: String,
    circuitBreakerRegistry: CircuitBreakerRegistry? = null,
    rateLimiterRegistry: RateLimiterRegistry? = null,
    private val retryConfig: RetryConfig? = null,
    cache: Cache<String, ByteArray>? = null,
    timeLimiterConfig: TimeLimiterConfig? = null
) {
    private val resilience =
        TmdbClientResilience(
            httpClient,
            objectMapper,
            circuitBreakerRegistry,
            rateLimiterRegistry,
            retryConfig,
            cache,
            timeLimiterConfig
        )
}
