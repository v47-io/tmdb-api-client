/**
 * The Clear BSD License
 *
 * Copyright (c) 2022 the tmdb-api-client authors
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
package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.TvEpisodeChanges
import io.v47.tmdb.model.TvEpisodeCredits
import io.v47.tmdb.model.TvEpisodeDetails
import io.v47.tmdb.model.TvEpisodeExternalIds
import io.v47.tmdb.model.TvEpisodeImages
import io.v47.tmdb.model.TvEpisodeTranslations
import io.v47.tmdb.model.TvEpisodeVideos
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate

class TvEpisodesApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get the TV episode details by id.
     *
     * Supports `appendToResponse` using the enumeration [TvEpisodeRequest] (More information
     * [here](https://developers.themoviedb.org/3/getting-started/append-to-response))
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     * @param language A language code
     * @param append Other requests to append to this call
     */
    fun details(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null,
        vararg append: TvEpisodeRequest
    ) =
        http.getWithLanguage<TvEpisodeDetails>(
            "/tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}",
            language
        ) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
            pathVar("episodeNumber", episodeNumber)

            append.forEach { req ->
                queryArg("append_to_response", req.value)
            }
        }

    /**
     * Get the changes for a TV episode. By default only the last 24 hours are returned.
     *
     * You can query up to 14 days in a single query by using the `startDate` and
     * `endDate` query parameters
     *
     * @param episodeId The id of the TV episode
     * @param startDate Filter the results with a start date
     * @param endDate Filter the results with an end date
     * @param page Specify which page to query
     */
    fun changes(
        episodeId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int? = null
    ) =
        http.getWithPage<TvEpisodeChanges>("/tv/episode/{episodeId}/changes", page) {
            pathVar("episodeId", episodeId)

            startDate?.let { queryArg("start_date", it.format(dateFormat)) }
            endDate?.let { queryArg("end_date", it.format(dateFormat)) }
        }


    /**
     * Get the credits (cast, crew and guest stars) for a TV episode
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     */
    fun credits(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null
    ) =
        http.getWithLanguage<TvEpisodeCredits>(
            "/tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}/credits",
            language
        ) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
            pathVar("episodeNumber", episodeNumber)
        }


    /**
     * Get the external ids for a TV episode.
     *
     * Look [here](https://developers.themoviedb.org/3/tv-seasons/get-tv-episode-external-ids)
     * for a list of supported external sources
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     */
    fun externalIds(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) =
        http.get<TvEpisodeExternalIds>("/tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}/external_ids") {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
            pathVar("episodeNumber", episodeNumber)
        }


    /**
     * Get the images that belong to a TV episode.
     *
     * Querying images with a `language` parameter will filter the results. If you
     * want to include a fallback language (especially useful for backdrops) you
     * can use the `includeImageLanguage` parameter. This should be a comma seperated
     * value like so: `en,null`
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     * @param language A language code
     * @param includeLanguage A list of language codes to filter the results
     */
    fun images(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null,
        vararg includeLanguage: LocaleCode?
    ) =
        http.getWithLanguage<TvEpisodeImages>(
            "/tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}/images",
            language
        ) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
            pathVar("episodeNumber", episodeNumber)

            includeLanguage.toSet().forEach { lang ->
                queryArg("include_image_language", lang?.toString() ?: "null")
            }
        }

    /**
     * Get the translation data for an episode
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     */
    fun translations(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) =
        http.get<TvEpisodeTranslations>("/tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}/translations") {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
            pathVar("episodeNumber", episodeNumber)
        }


    /**
     * Get the videos that have been added to a TV episode
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     * @param language A language code
     */
    fun videos(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null
    ) =
        http.getWithLanguage<TvEpisodeVideos>(
            "/tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}/videos",
            language
        ) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
            pathVar("episodeNumber", episodeNumber)
        }
}

enum class TvEpisodeRequest(internal val value: String) {
    Changes("changes"),
    Credits("credits"),
    ExternalIds("external_ids"),
    Images("images"),
    Translations("translations"),
    Videos("videos")
}
