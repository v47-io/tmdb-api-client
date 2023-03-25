/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb

import io.smallrye.mutiny.Uni
import io.v47.tmdb.api.CertificationsApi
import io.v47.tmdb.api.ChangesApi
import io.v47.tmdb.api.CollectionApi
import io.v47.tmdb.api.CompanyApi
import io.v47.tmdb.api.ConfigurationApi
import io.v47.tmdb.api.CreditApi
import io.v47.tmdb.api.DiscoverApi
import io.v47.tmdb.api.FindApi
import io.v47.tmdb.api.GenresApi
import io.v47.tmdb.api.ImagesApi
import io.v47.tmdb.api.KeywordApi
import io.v47.tmdb.api.ListApi
import io.v47.tmdb.api.MoviesApi
import io.v47.tmdb.api.NetworksApi
import io.v47.tmdb.api.PeopleApi
import io.v47.tmdb.api.ReviewsApi
import io.v47.tmdb.api.SearchApi
import io.v47.tmdb.api.TrendingApi
import io.v47.tmdb.api.TvApi
import io.v47.tmdb.api.TvEpisodeGroupsApi
import io.v47.tmdb.api.TvEpisodesApi
import io.v47.tmdb.api.TvSeasonsApi
import io.v47.tmdb.api.key.TmdbApiKeyProvider
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Configuration
import io.v47.tmdb.utils.OpenTmdbClient

/**
 * The main entry point for the TMDb API Client. Here all supported resources of the TMDb API can be
 * accessed.
 */
@Suppress("ConstructorParameterNaming")
@OpenTmdbClient
class TmdbClient private constructor(
    private val httpClientFactory: HttpClientFactory,
    httpExecutor: HttpExecutor
) {
    /**
     * Creates a [TmdbClient] using the specified [HttpClientFactory] and authenticating all
     * requests using the specified API-Key.
     *
     * @param httpClientFactory
     * @param apiKey
     */
    constructor (httpClientFactory: HttpClientFactory, apiKey: String) :
            this(httpClientFactory, TmdbApiKeyProvider { apiKey })

    /**
     * Creates a [TmdbClient] using the specified [HttpClientFactory] and authenticating all
     * requests using an API-Key provided by the specified [TmdbApiKeyProvider].
     *
     * The returned API-Key will not be cached, this is left to the actual provider implementation
     * to enable scenarios where the API-Key may change dynamically.
     *
     * @param httpClientFactory
     * @param apiKeyProvider
     */
    constructor (httpClientFactory: HttpClientFactory, apiKeyProvider: TmdbApiKeyProvider) :
            this(httpClientFactory, HttpExecutor(httpClientFactory, apiKeyProvider))

    private var _cachedSystemConfiguration: Configuration? = null

    /**
     * Provides a variant of [ConfigurationApi.system] which is cached as runtime to reduce network
     * requests for a resource that won't often change.
     */
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
                Uni
                    .createFrom()
                    .publisher(configuration.system())
                    .onItem()
                    .invoke { config ->
                        _cachedSystemConfiguration = config
                        _images = ImagesApi(httpClientFactory, _cachedSystemConfiguration!!)
                    }
                    .onFailure().recoverWithNull()
                    .await().indefinitely()
            }
        }
    }

    fun refreshCachedConfiguration(): Uni<Unit> =
        Uni
            .createFrom()
            .publisher(configuration.system())
            .onItem()
            .invoke { systemConfig ->
                synchronized(this) {
                    _cachedSystemConfiguration = systemConfig

                    _images?.close()
                    _images = ImagesApi(httpClientFactory, systemConfig)
                }
            }
            .onFailure().recoverWithNull()
            .map { }
}
