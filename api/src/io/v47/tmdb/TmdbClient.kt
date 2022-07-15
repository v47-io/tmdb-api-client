/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
                synchronized(this) {
                    _cachedSystemConfiguration = systemConfig

                    _images?.close()
                    _images = ImagesApi(httpClientFactory, systemConfig)
                }
            }
            .map {}
}
