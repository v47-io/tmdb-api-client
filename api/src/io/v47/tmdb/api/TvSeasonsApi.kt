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
package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.*
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate

class TvSeasonsApi internal constructor(private val httpExecutor: HttpExecutor) {
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
        httpExecutor.execute(
            getWithLanguage<TvSeasonDetails>(
                "/tv/{tvId}/season/{seasonNumber}",
                language
            ) {
                pathVar("tvId", tvId)
                pathVar("seasonNumber", seasonNumber)

                append.forEach { req ->
                    queryArg("append_to_response", req.value)
                }
            }
        )

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
        httpExecutor.execute(
            getWithPage<TvSeasonChanges>("/tv/season/{seasonId}/changes", page) {
                pathVar("seasonId", seasonId)

                startDate?.let { queryArg("start_date", it.format(dateFormat)) }
                endDate?.let { queryArg("end_date", it.format(dateFormat)) }
            }
        )

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
        httpExecutor.execute(
            getWithLanguage<TvSeasonCredits>(
                "/tv/{tvId}/season/{seasonNumber}/credits",
                language
            ) {
                pathVar("tvId", tvId)
                pathVar("seasonNumber", seasonNumber)
            }
        )

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
        httpExecutor.execute(
            get<TvSeasonExternalIds>("/tv/{tvId}/season/{seasonNumber}/external_ids") {
                pathVar("tvId", tvId)
                pathVar("seasonNumber", seasonNumber)
            }
        )

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
        httpExecutor.execute(
            getWithLanguage<TvSeasonImages>("/tv/{tvId}/season/{seasonNumber}/images", language) {
                pathVar("tvId", tvId)
                pathVar("seasonNumber", seasonNumber)

                includeLanguage.toSet().forEach { lang ->
                    queryArg("include_image_language", lang?.toString() ?: "null")
                }
            }
        )

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
        httpExecutor.execute(
            getWithLanguage<TvSeasonVideos>("/tv/{tvId}/season/{seasonNumber}/videos", language) {
                pathVar("tvId", tvId)
                pathVar("seasonNumber", seasonNumber)
            }
        )
}

enum class TvSeasonRequest(internal val value: String) {
    Changes("changes"),
    Credits("credits"),
    ExternalIds("external_ids"),
    Images("images"),
    Videos("videos")
}
