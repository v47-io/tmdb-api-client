package io.v47.tmdb

import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.reactivex.Single
import io.v47.tmdb.api.*
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Configuration
import java.time.Duration

@Suppress("ConstructorParameterNaming")
class TmdbClient private constructor(
    private val httpClientFactory: HttpClientFactory,
    httpExecutor: HttpExecutor,
    private var _cachedSystemConfiguration: Configuration,
    private val timeLimiterConfig: TimeLimiterConfig
) {
    companion object {
        @JvmStatic
        fun blockingCreate(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ) = create(httpClientFactory, apiKey, rateLimiterRegistry, timeLimiterConfig).blockingGet()!!

        @JvmStatic
        fun create(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ): Single<TmdbClient> {
            val tlConfig = timeLimiterConfig
                ?: TimeLimiterConfig.custom()
                    .cancelRunningFuture(true)
                    .timeoutDuration(Duration.ofSeconds(30))
                    .build()

            val httpExecutor = createHttpExecutor(httpClientFactory, apiKey, rateLimiterRegistry, tlConfig)
            val configurationApi = ConfigurationApi(httpExecutor)

            return Single.fromPublisher(configurationApi.system())
                .map { TmdbClient(httpClientFactory, httpExecutor, it, tlConfig) }
        }

        private fun createHttpExecutor(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig
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

    private var _images = ImagesApi(httpClientFactory, _cachedSystemConfiguration, timeLimiterConfig)
    val images get() = _images

    val keyword = KeywordApi(httpExecutor)
    val list = ListApi(httpExecutor)

    fun refreshCachedConfiguration(): Single<Unit> =
        Single
            .fromPublisher(configuration.system())
            .doOnSuccess { systemConfig ->
                _cachedSystemConfiguration = systemConfig

                _images.close()
                _images = ImagesApi(httpClientFactory, systemConfig, timeLimiterConfig)
            }
            .map { Unit }
}
