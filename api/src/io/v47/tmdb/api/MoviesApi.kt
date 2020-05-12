/**
 * Copyright 2020 The tmdb-api-v2 Authors
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

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.*
import io.v47.tmdb.utils.checkPage
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate

@Suppress("TooManyFunctions")
class MoviesApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the primary information about a movie.
     *
     * Supports `appendToResponse` using the enumeration [MovieRequest] (More information
     * [here](https://developers.themoviedb.org/3/getting-started/append-to-response))
     *
     * @param movieId The id of the movie
     * @param language A language code
     * @param append Other requests to append to this call
     */
    fun details(movieId: Int, language: LocaleCode? = null, vararg append: MovieRequest) =
        httpExecutor.execute(
            getWithLanguage<MovieDetails>("/movie/{movieId}", language) {
                pathVar("movieId", movieId)

                append.forEach { req ->
                    queryArg("append_to_response", req.value)
                }
            }
        )

    /**
     * Get all of the alternative titles for a movie
     *
     * @param movieId The id of the movie
     * @param country A country code
     */
    fun alternativeTitles(movieId: Int, country: CountryCode? = null) =
        httpExecutor.execute(
            get<MovieAlternativeTitles>("/movie/{movieId}/alternative_titles") {
                pathVar("movieId", movieId)

                country?.let { queryArg("country", country.toString()) }
            }
        )

    /**
     * Get the changes for a movie. By default only the last 24 hours are returned.
     *
     * You can query up to 14 days in a single query by using the `startDate` and `endDate` parameters
     *
     * @param movieId The id of the movie
     * @param startDate Filter the results with a start date
     * @param endDate Filter the results with an end date
     * @param page Specify which page to query
     */
    fun changes(
        movieId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int? = null
    ) =
        httpExecutor.execute(
            get<MovieChanges>("/movie/{movieId}/changes") {
                pathVar("movieId", movieId)

                page?.let {
                    checkPage(it)
                    queryArg("page", it)
                }

                startDate?.let { queryArg("start_date", it.format(dateFormat)) }
                endDate?.let { queryArg("end_date", it.format(dateFormat)) }
            }
        )

    /**
     * Get the cast and crew for a movie
     *
     * @param movieId The id of the movie
     */
    fun credits(movieId: Int) =
        httpExecutor.execute(
            get<MovieCredits>("/movie/{movieId}/credits") {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get the external ids for a movie
     *
     * See the supported external sources
     * [here](https://developers.themoviedb.org/3/movies/get-movie-external-ids)
     *
     * @param movieId The id of the movie
     */
    fun externalIds(movieId: Int) =
        httpExecutor.execute(
            get<MovieExternalIds>("/movie/{movieId}/external_ids") {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get the images that belong to a movie.
     *
     * Querying images with a `language` parameter will filter the results. If you
     * want to include a fallback language (especially useful for backdrops) you
     * can use the `includeImageLanguage` parameter. This should be a comma seperated
     * value like so: `en,null`
     *
     * @param movieId The id of the movie
     * @param language A language code
     * @param includeLanguage A list of language codes to filter the results
     */
    fun images(movieId: Int, language: LocaleCode? = null, vararg includeLanguage: LocaleCode?) =
        httpExecutor.execute(
            getWithLanguage<MovieImages>("/movie/{movieId}/images", language) {
                pathVar("movieId", movieId)

                includeLanguage.toSet().forEach { lang ->
                    queryArg("include_image_language", lang?.toString() ?: "null")
                }
            }
        )

    /**
     * Get the keywords that have been added to a movie
     *
     * @param movieId The id of the movie
     */
    fun keywords(movieId: Int) =
        httpExecutor.execute(
            get<MovieKeywords>("/movie/{movieId}/keywords") {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get the release date along with the certification for a movie.
     *
     * Release dates support different types:
     *
     * 1. Premiere
     * 2. Theatrical (limited)
     * 3. Theatrical
     * 4. Digital
     * 5. Physical
     * 6. TV
     *
     * @param movieId The id of the movie
     */
    fun releaseDates(movieId: Int) =
        httpExecutor.execute(
            get<MovieReleaseDates>("/movie/{movieId}/release_dates") {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get the videos that have been added to a movie
     *
     * @param movieId The id of the movie
     * @param language A language code
     */
    fun videos(movieId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<MovieVideos>("/movie/{movieId}/videos", language) {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get a list of translations that have been created for a movie
     *
     * @param movieId The id of the movie
     */
    fun translations(movieId: Int) =
        httpExecutor.execute(
            get<MovieTranslations>("/movie/{movieId}/translations") {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get a list of recommended movies for a movie
     *
     * @param movieId The id of the movie
     * @param language A language code
     * @param page Specify which page to query
     */
    fun recommendations(movieId: Int, page: Int? = null, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithPageAndLanguage<PaginatedListResults<MovieListResult>>(
                "/movie/{movieId}/recommendations",
                page,
                language
            ) {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get a list of similar movies. This is not the same as the "Recommendation"
     * system you see on the website.
     *
     * These items are assembled by looking at keywords and genres
     *
     * @param movieId The id of the movie
     * @param language A language code
     * @param page Specify which page to query
     */
    fun similar(movieId: Int, page: Int? = null, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithPageAndLanguage<PaginatedListResults<MovieListResult>>(
                "/movie/{movieId}/similar",
                page,
                language
            ) {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get the user reviews for a movie
     *
     * @param movieId The id of the movie
     * @param language An `ISO 639-1` value to display translated data for the fields that support it
     * @param page Specify which page to query
     */
    fun reviews(movieId: Int, page: Int? = null, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithPageAndLanguage<MovieReviews>(
                "/movie/{movieId}/reviews",
                page,
                language
            ) {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get a list of lists that this movie belongs to
     *
     * @param movieId The id of the movie
     * @param language A language code
     * @param page Specify which page to query
     */
    fun lists(movieId: Int, page: Int? = null, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithPageAndLanguage<MovieLists>(
                "/movie/{movieId}/lists",
                page,
                language
            ) {
                pathVar("movieId", movieId)
            }
        )

    /**
     * Get the most newly created movie. This is a live response and will continuously change
     *
     * @param language A language code
     */
    fun latest(language: LocaleCode? = null) =
        httpExecutor.execute(getWithLanguage<MovieDetails>("/movie/latest", language))

    /**
     * Get a list of movies in theatres. This is a release type query that looks for all
     * meovies that have a release type of 2 or 3 within the specified date range.
     *
     * You can optionally specify a `region` prameter which will narrow the search to only look
     * for theatrical release dates within the specified country
     *
     * @param language A language code
     * @param page Specify which page to query
     * @param region Specify a country code to filter release dates
     */
    fun nowPlaying(page: Int? = null, language: LocaleCode? = null, region: CountryCode? = null) =
        getMovieListWithDetails("/movie/now_playing", page, language, region)

    /**
     * Get a list of the current popular movies on TMDb. This list updates daily
     *
     * @param language A language code
     * @param page Specify which page to query
     * @param region Specify a country code to filter release dates
     */
    fun popular(page: Int? = null, language: LocaleCode? = null, region: CountryCode? = null) =
        getMovieListWithDetails("/movie/popular", page, language, region)

    /**
     * Get the top rated movies on TMDb
     *
     * @param language A language code
     * @param page Specify which page to query
     * @param region Specify a country code to filter release dates
     */
    fun topRated(page: Int? = null, language: LocaleCode? = null, region: CountryCode? = null) =
        getMovieListWithDetails("/movie/top_rated", page, language, region)

    /**
     * Get a list of upcoming movies in theatres. This is a release type query that looks for all
     * movies that have a release type of 2 or 3 within the specified date range.
     *
     * You can optionally specify a `region` prameter which will narrow the search to only look for
     * theatrical release dates within the specified country
     *
     * @param language A language code
     * @param page Specify which page to query
     * @param region Specify a country code to filter release dates
     */
    fun upcoming(page: Int? = null, language: LocaleCode? = null, region: CountryCode? = null) =
        getMovieListWithDetails("/movie/upcoming", page, language, region)

    private fun getMovieListWithDetails(
        path: String,
        page: Int? = null,
        language: LocaleCode? = null,
        region: CountryCode? = null
    ) =
        httpExecutor.execute(
            getWithPageAndLanguage<PaginatedMovieListResultsWithDates>(path, page, language) {
                region?.let { queryArg("region", it.toString()) }
            }
        )
}

enum class MovieRequest(internal val value: String) {
    AlternativeTitles("alternative_titles"),
    Changes("changes"),
    Credits("credits"),
    ExternalIds("external_ids"),
    Images("images"),
    Keywords("keywords"),
    ReleaseDates("release_dates"),
    Videos("videos"),
    Translations("translations"),
    Recommendations("recommendations"),
    Similar("similar"),
    Reviews("reviews"),
    Lists("lists")
}
