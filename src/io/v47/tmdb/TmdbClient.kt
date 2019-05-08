package io.v47.tmdb

import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.reactivex.Flowable
import io.reactivex.Single
import io.v47.tmdb.api.ConfigurationApi
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Configuration

class TmdbClient private constructor(
    private val httpExecutor: HttpExecutor,
    private var cachedConfiguration: Configuration
) {
    companion object {
        fun new(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ): TmdbClient {
            val httpExecutor = createHttpExecutor(httpClientFactory, apiKey, rateLimiterRegistry, timeLimiterConfig)

            val configurationApi = ConfigurationApi(httpExecutor)
            val config = Flowable.fromPublisher(configurationApi.getConfiguration()).blockingFirst()

            return TmdbClient(httpExecutor, config)
        }

        fun newRx(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ): Single<TmdbClient> {
            val httpExecutor = createHttpExecutor(httpClientFactory, apiKey, rateLimiterRegistry, timeLimiterConfig)
            val configurationApi = ConfigurationApi(httpExecutor)

            return Single.fromPublisher(configurationApi.getConfiguration())
                .map { TmdbClient(httpExecutor, it) }
        }

        private fun createHttpExecutor(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ) =
            HttpExecutor(
                httpClientFactory,
                apiKey,
                rateLimiterRegistry,
                timeLimiterConfig
            )
    }


}
