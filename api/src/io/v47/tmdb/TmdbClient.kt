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
    private val timeLimiterConfig: TimeLimiterConfig
) {
    companion object {
        operator fun invoke(
            httpClientFactory: HttpClientFactory,
            apiKey: String,
            rateLimiterRegistry: RateLimiterRegistry? = null,
            timeLimiterConfig: TimeLimiterConfig? = null
        ): TmdbClient {
            @Suppress("MagicNumber")
            val tlConfig = timeLimiterConfig
                ?: TimeLimiterConfig.custom()
                    .cancelRunningFuture(true)
                    .timeoutDuration(Duration.ofSeconds(30))
                    .build()

            val httpExecutor = HttpExecutor(
                httpClientFactory,
                apiKey,
                rateLimiterRegistry,
                tlConfig
            )

            return TmdbClient(httpClientFactory, httpExecutor, tlConfig)
        }
    }

    private var _cachedSystemConfiguration: Configuration? = null
    val cachedSystemConfiguration: Configuration
        get() {
            if (_cachedSystemConfiguration == null)
                initialize()

            return _cachedSystemConfiguration!!
        }

    val certifications = CertificationsApi(httpExecutor)
    val changes = ChangesApi(httpExecutor)
    val collection = CollectionApi(httpExecutor)
    val company = CompanyApi(httpExecutor)
    val configuration = ConfigurationApi(httpExecutor)
    val credits = CreditApi(httpExecutor)
    val discover = DiscoverApi(httpExecutor)
    val find = FindApi(httpExecutor)
    val genres = GenresApi(httpExecutor)

    private var _images: ImagesApi? = null
    val images: ImagesApi
        get() {
            if (_images == null)
                initialize()

            return _images!!
        }

    val keyword = KeywordApi(httpExecutor)
    val list = ListApi(httpExecutor)
    val movie = MoviesApi(httpExecutor)
    val network = NetworksApi(httpExecutor)
    val person = PeopleApi(httpExecutor)
    val review = ReviewsApi(httpExecutor)
    val search = SearchApi(httpExecutor)
    val trending = TrendingApi(httpExecutor)
    val tvShow = TvApi(httpExecutor)
    val tvEpisode = TvEpisodesApi(httpExecutor)
    val tvEpisodeGroup = TvEpisodeGroupsApi(httpExecutor)
    val tvSeason = TvSeasonsApi(httpExecutor)

    private fun initialize() {
        synchronized(this) {
            if (_cachedSystemConfiguration == null) {
                Single.fromPublisher(configuration.system())
                    .map { config ->
                        _cachedSystemConfiguration = config
                        _images = ImagesApi(httpClientFactory, _cachedSystemConfiguration!!, timeLimiterConfig)
                    }
                    .blockingGet()
            }
        }
    }

    fun refreshCachedConfiguration(): Single<Unit> =
        Single
            .fromPublisher(configuration.system())
            .doOnSuccess { systemConfig ->
                _cachedSystemConfiguration = systemConfig

                _images?.close()
                _images = ImagesApi(httpClientFactory, systemConfig, timeLimiterConfig)
            }
            .map { Unit }
}
