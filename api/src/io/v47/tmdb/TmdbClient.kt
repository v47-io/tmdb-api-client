/**
 * Copyright 2022 The tmdb-api-client Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.v47.tmdb

import io.reactivex.rxjava3.core.Single
import io.v47.tmdb.api.*
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Configuration

@Suppress("ConstructorParameterNaming")
class TmdbClient private constructor(
    private val httpClientFactory: HttpClientFactory,
    httpExecutor: HttpExecutor
) {
    constructor (httpClientFactory: HttpClientFactory, apiKey: String) :
            this(httpClientFactory, HttpExecutor(httpClientFactory, apiKey))

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
                        _images = ImagesApi(httpClientFactory, _cachedSystemConfiguration!!)
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
                _images = ImagesApi(httpClientFactory, systemConfig)
            }
            .map { Unit }
}
