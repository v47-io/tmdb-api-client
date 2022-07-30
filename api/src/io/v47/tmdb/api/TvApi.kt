/**
 * The Clear BSD License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
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
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.PaginatedListResults
import io.v47.tmdb.model.TvListResult
import io.v47.tmdb.model.TvShowAlternativeTitles
import io.v47.tmdb.model.TvShowChanges
import io.v47.tmdb.model.TvShowContentRatings
import io.v47.tmdb.model.TvShowCredits
import io.v47.tmdb.model.TvShowDetails
import io.v47.tmdb.model.TvShowEpisodeGroups
import io.v47.tmdb.model.TvShowExternalIds
import io.v47.tmdb.model.TvShowImages
import io.v47.tmdb.model.TvShowKeywords
import io.v47.tmdb.model.TvShowReview
import io.v47.tmdb.model.TvShowScreenedTheatrically
import io.v47.tmdb.model.TvShowTranslations
import io.v47.tmdb.model.TvShowVideos
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate

@Suppress("TooManyFunctions")
class TvApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get the primary TV show details by id.
     *
     * Supports `appendToResponse` using the enumeration [TvRequest] (More information
     * [here](https://developers.themoviedb.org/3/getting-started/append-to-response))
     *
     * @param tvId The id of the TV show
     * @param language A language code
     * @param append Other requests to append to this call
     */
    fun details(tvId: Int, language: LocaleCode? = null, vararg append: TvRequest) =
        http.getWithLanguage<TvShowDetails>("/tv/{tvId}", language) {
            pathVar("tvId", tvId)

            append.forEach { req ->
                queryArg("append_to_response", req.value)
            }
        }


    /**
     * Returns all of the alternative titles for a TV show
     *
     * @param tvId The id of the TV show
     * @param language A language code
     */
    fun alternativeTitles(tvId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<TvShowAlternativeTitles>("/tv/{tvId}/alternative_titles", language) {
            pathVar("tvId", tvId)
        }


    /**
     * Get the changes for a TV show. By default only the last 24 hours are returned.
     *
     * You can query up to 14 days in a single query by using the `startDate`
     * and `endDate` parameters.
     *
     * TV show changes are different than movie changes in that there are some edits
     * on seasons and episodes that will create a change entry at the show level.
     * These can be found under the season and episode keys. These keys will contain
     * a `seriesId` and `episodeId`. You can use the [TvSeasonsApi.changes] and
     * [TvEpisodesApi.changes] methods to look these up individually.
     *
     * @param tvId The id of the TV show
     * @param startDate Filter the results with a start date
     * @param endDate Filter the results with an end date
     * @param page Specify which page to query
     */
    fun changes(
        tvId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int? = null
    ) =
        http.getWithPage<TvShowChanges>("/tv/{tvId}/changes", page) {
            pathVar("tvId", tvId)

            startDate?.let { queryArg("start_date", it.format(dateFormat)) }
            endDate?.let { queryArg("end_date", it.format(dateFormat)) }
        }


    /**
     * Get the list of content ratings (certifications) that have been added to a TV show
     *
     * @param tvId The id of the TV show
     * @param language A language code
     */
    fun contentRatings(tvId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<TvShowContentRatings>("/tv/{tvId}/content_ratings", language) {
            pathVar("tvId", tvId)
        }


    /**
     * Get the credits (cast and crew) that have been added to a TV show
     *
     * @param tvId The id of the TV show
     * @param language A language code
     */
    fun credits(tvId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<TvShowCredits>("/tv/{tvId}/credits", language) {
            pathVar("tvId", tvId)
        }


    /**
     * Get all of the episode groups that have been created for a TV show.
     * With a group ID you can call the [TvEpisodeGroupsApi.details] method
     *
     * @param tvId The id of the TV show
     * @param language A language code
     */
    fun episodeGroups(tvId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<TvShowEpisodeGroups>("/tv/{tvId}/episode_groups", language) {
            pathVar("tvId", tvId)
        }


    /**
     * Get the external ids for a TV show
     *
     * See the supported external sources
     * [here](https://developers.themoviedb.org/3/movies/get-tv-external-ids)
     *
     * @param tvId The id of the movie
     */
    fun externalIds(tvId: Int) =
        http.get<TvShowExternalIds>("/tv/{tvId}/external_ids") {
            pathVar("tvId", tvId)
        }


    /**
     * Get the images that belong to a TV show.
     *
     * Querying images with a `language` parameter will filter the results. If you
     * want to include a fallback language (especially useful for backdrops) you
     * can use the `includeImageLanguage` parameter. This should be a comma seperated
     * value like so: `en,null`
     *
     * @param tvId The id of the TV show
     * @param language A language code
     * @param includeLanguage A list of language codes to filter the results
     */
    fun images(tvId: Int, language: LocaleCode? = null, vararg includeLanguage: LocaleCode?) =
        http.getWithLanguage<TvShowImages>("/tv/{tvId}/images", language) {
            pathVar("tvId", tvId)

            includeLanguage.toSet().forEach { lang ->
                queryArg("include_image_language", lang?.toString() ?: "null")
            }
        }


    /**
     * Get the keywords that have been added to a TV show
     *
     * @param tvId The id of the TV show
     */
    fun keywords(tvId: Int) =
        http.get<TvShowKeywords>("/tv/{tvId}/keywords") {
            pathVar("tvId", tvId)
        }


    /**
     * Get the list of TV show recommendations for this item
     *
     * @param tvId The id of the TV show
     * @param language A language code
     * @param page Specify which page to query
     */
    fun recommendations(tvId: Int, page: Int? = null, language: LocaleCode? = null) =
        http.getWithPageAndLanguage<PaginatedListResults<TvListResult>>(
            "/tv/{tvId}/recommendations",
            page,
            language
        ) {
            pathVar("tvId", tvId)
        }

    /**
     * Get the reviews for a TV show
     *
     * @param tvId The id of the TV show
     * @param language A language code
     * @param page Specify which page to query
     */
    fun reviews(tvId: Int, page: Int? = null, language: LocaleCode? = null) =
        http.getWithPageAndLanguage<PaginatedListResults<TvShowReview>>(
            "/tv/{tvId}/reviews",
            page,
            language
        ) {
            pathVar("tvId", tvId)
        }

    /**
     * Get a list of seasons or episodes that have been screened in a film festival or theatre
     */
    fun screenedTheatrically(tvId: Int) =
        http.get<TvShowScreenedTheatrically>("/tv/{tvId}/screened_theatrically") {
            pathVar("tvId", tvId)
        }


    /**
     * Get a list of similar TV shows. These items are assembled by looking at keywords and genres
     *
     * @param tvId The id of the TV show
     * @param language A language code
     * @param page Specify which page to query
     */
    fun similar(tvId: Int, page: Int? = null, language: LocaleCode? = null) =
        http.getWithPageAndLanguage<PaginatedListResults<TvListResult>>(
            "/tv/{tvId}/similar",
            page,
            language
        ) {
            pathVar("tvId", tvId)
        }

    /**
     * Get a list of the translations that exist for a TV show
     *
     * @param tvId The id of the TV show
     * @param language A language code
     */
    fun translations(tvId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<TvShowTranslations>("/tv/{tvId}/translations", language) {
            pathVar("tvId", tvId)
        }


    /**
     * Get the videos that have been added to a TV show
     *
     * @param tvId The id of the TV show
     * @param language A language code
     */
    fun videos(tvId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<TvShowVideos>("/tv/{tvId}/videos", language) {
            pathVar("tvId", tvId)
        }


    /**
     * Get the most newly created TV show. This is a live response and will continuously change
     *
     * @param language A language code
     */
    fun latest(language: LocaleCode? = null) =
        http.getWithLanguage<TvShowDetails>("/tv/latest", language)

    /**
     * Get a list of TV shows that are airing today. This query is purely day based as we
     * do not currently support airing times.
     *
     * @param language A language code
     * @param page Specify which page to query
     */
    fun getAiringToday(page: Int? = null, language: LocaleCode? = null) =
        getPaginatedTvList("/tv/airing_today", page, language)

    /**
     * Get a list of shows that are currently on the air.
     *
     * This query looks for any TV show that has an episode with an air date in the next 7 days.
     *
     * @param language A language code
     * @param page Specify which page to query
     */
    fun getOnTheAir(page: Int? = null, language: LocaleCode? = null) =
        getPaginatedTvList("/tv/on_the_air", page, language)

    /**
     * Get a list of the current popular TV shows on TMDb. This list updates daily
     *
     * @param language A language code
     * @param page Specify which page to query
     */
    fun getPopular(page: Int? = null, language: LocaleCode? = null) =
        getPaginatedTvList("/tv/popular", page, language)

    /**
     * Get a list of the top rated TV shows on TMDb
     *
     * @param language A language code
     * @param page Specify which page to query
     */
    fun getTopRated(page: Int? = null, language: LocaleCode? = null) =
        getPaginatedTvList("/tv/top_rated", page, language)

    private fun getPaginatedTvList(path: String, page: Int?, language: LocaleCode?) =
        http.getWithPageAndLanguage<PaginatedListResults<TvListResult>>(
            path,
            page,
            language

        )
}

enum class TvRequest(internal val value: String) {
    AlternativeTitles("alternative_titles"),
    Changes("changes"),
    ContentRatings("content_ratings"),
    Credits("credits"),
    ExternalIds("external_ids"),
    Images("images"),
    Keywords("keywords"),
    Recommendations("recommendations"),
    ScreenedTheatrically("screened_theatrically"),
    Similar("similar"),
    Translations("translations"),
    Videos("videos")
}
