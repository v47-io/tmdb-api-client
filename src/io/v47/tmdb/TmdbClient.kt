package io.v47.tmdb

import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.reactivex.Single
import io.v47.tmdb.api.*
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Configuration

@Suppress("ConstructorParameterNaming")
class TmdbClient private constructor(
    private val httpClientFactory: HttpClientFactory,
    httpExecutor: HttpExecutor,
    private var _cachedSystemConfiguration: Configuration
) {
    companion object {
        fun blockingNew(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ) = new(httpClientFactory, apiKey, rateLimiterRegistry, timeLimiterConfig).blockingGet()!!

        fun new(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ): Single<TmdbClient> {
            val httpExecutor = createHttpExecutor(httpClientFactory, apiKey, rateLimiterRegistry, timeLimiterConfig)
            val configurationApi = ConfigurationApi(httpExecutor)

            return Single.fromPublisher(configurationApi.system())
                .map { TmdbClient(httpClientFactory, httpExecutor, it) }
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

    val cachedSystemConfiguration
        get() = _cachedSystemConfiguration

    val certifications = CertificationsApi(httpExecutor)
    val changes = ChangesApi(httpExecutor)
    val collection = CollectionApi(httpExecutor)
    val company = CompanyApi(httpExecutor)
    val configuration = ConfigurationApi(httpExecutor)
    val credit = CreditApi(httpExecutor)
    val discover = DiscoverApi(httpExecutor)
    val find = FindApi(httpExecutor)
    val genres = GenresApi(httpExecutor)

    private var _images = ImagesApi(httpClientFactory, _cachedSystemConfiguration)
    val images get() = _images

    fun refreshCachedConfiguration(): Single<Unit> =
        Single
            .fromPublisher(configuration.system())
            .doOnSuccess { systemConfig ->
                _cachedSystemConfiguration = systemConfig

                _images.close()
                _images = ImagesApi(httpClientFactory, systemConfig)
            }
            .map { Unit }
}
