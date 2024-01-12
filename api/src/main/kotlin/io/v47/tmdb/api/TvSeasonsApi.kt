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
package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.TvSeasonChanges
import io.v47.tmdb.model.TvSeasonCredits
import io.v47.tmdb.model.TvSeasonDetails
import io.v47.tmdb.model.TvSeasonExternalIds
import io.v47.tmdb.model.TvSeasonImages
import io.v47.tmdb.model.TvSeasonVideos
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate

class TvSeasonsApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get the TV season details by id.
     *
     * Supports `appendToResponse` using the enumeration [TvSeasonRequest] (More information
     * [here](https://developers.themoviedb.org/3/getting-started/append-to-response))
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param language A language code
     * @param append Other methods to append to this call
     */
    fun details(
        tvId: Int,
        seasonNumber: Int,
        language: LocaleCode? = null,
        vararg append: TvSeasonRequest
    ) =
        http.getWithLanguage<TvSeasonDetails>(
            "/tv/{tvId}/season/{seasonNumber}",
            language
        ) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)

            append.forEach { req ->
                queryArg("append_to_response", req.value)
            }
        }

    /**
     * Get the changes for a TV season. By default only the last 24 hours are returned.
     *
     * You can query up to 14 days in a single query by using the `startDate` and `endDate` parameters.
     *
     * @param seasonId The id of the TV season
     * @param startDate Filter the results with a start date
     * @param endDate Filter the results with an end date
     * @param page Specify which page to query
     */
    fun changes(
        seasonId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int? = null
    ) =
        http.getWithPage<TvSeasonChanges>("/tv/season/{seasonId}/changes", page) {
            pathVar("seasonId", seasonId)

            startDate?.let { queryArg("start_date", it.format(dateFormat)) }
            endDate?.let { queryArg("end_date", it.format(dateFormat)) }
        }


    /**
     * Get the credits for TV season
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param language A language code
     */
    fun credits(
        tvId: Int,
        seasonNumber: Int,
        language: LocaleCode? = null
    ) =
        http.getWithLanguage<TvSeasonCredits>(
            "/tv/{tvId}/season/{seasonNumber}/credits",
            language
        ) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
        }

    /**
     * Get the external ids for a TV season.
     *
     * Look [here](https://developers.themoviedb.org/3/tv-seasons/get-tv-season-external-ids)
     * for a list of supported external sources
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     */
    fun externalIds(
        tvId: Int,
        seasonNumber: Int
    ) =
        http.get<TvSeasonExternalIds>("/tv/{tvId}/season/{seasonNumber}/external_ids") {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
        }


    /**
     * Get the images that belong to a TV season.
     *
     * Querying images with a `language` parameter will filter the results. If you
     * want to include a fallback language (especially useful for backdrops) you
     * can use the `includeImageLanguage` parameter. This should be a comma seperated
     * value like so: `en,null`
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param language A language code
     * @param includeLanguage A list of language codes to filter the results
     */
    fun images(
        tvId: Int,
        seasonNumber: Int,
        language: LocaleCode? = null,
        vararg includeLanguage: LocaleCode?
    ) =
        http.getWithLanguage<TvSeasonImages>("/tv/{tvId}/season/{seasonNumber}/images", language) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)

            includeLanguage.toSet().forEach { lang ->
                queryArg("include_image_language", lang?.toString() ?: "null")
            }
        }


    /**
     * Get the videos that have been added to a TV season
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param language A language code
     */
    fun videos(
        tvId: Int,
        seasonNumber: Int,
        language: LocaleCode? = null
    ) =
        http.getWithLanguage<TvSeasonVideos>("/tv/{tvId}/season/{seasonNumber}/videos", language) {
            pathVar("tvId", tvId)
            pathVar("seasonNumber", seasonNumber)
        }

}

enum class TvSeasonRequest(internal val value: String) {
    Changes("changes"),
    Credits("credits"),
    ExternalIds("external_ids"),
    Images("images"),
    Videos("videos")
}
