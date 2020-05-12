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
import com.neovisionaries.i18n.LanguageCode
import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.TmdbRequestBuilder
import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.MovieListResult
import io.v47.tmdb.model.PaginatedListResults
import io.v47.tmdb.model.TvListResult
import io.v47.tmdb.utils.checkPage
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate
import java.util.*

class DiscoverApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Discover movies by different types of data like average rating, number of
     * votes, genres and certifications. You can get a valid list of certifications
     * from the [CertificationsApi.forMovies] method.
     *
     * Discover also supports a nice list of sort options. See [MovieQuery] for all
     * of the available options.
     *
     * Please note, when using `certification` or `certification$lte` you must also
     * specify `certificationCountry`. These two parameters work together in order to
     * filter the results. You can only filter results with the countries we have added
     * to our certifications list.
     *
     * If you specify the `region` parameter, the regional release date will be used instead
     * of the primary release date. The date returned will be the first date based on your
     * query (ie. if a `withReleaseType` is specified). It's important to note the order of
     * the release types that are used. Specifying "2|3" would return the limited theatrical
     * release date as opposed to "3|2" which would return the theatrical date.
     *
     * Also note that a number of filters support being comma (`,`) or pipe (`|`) separated.
     * Comma's are treated like an `AND` and query while pipe's are an `OR`.
     *
     * Some examples of what can be done with discover can be found
     * [here](https://www.themoviedb.org/documentation/api/discover).
     */
    fun movies(block: MovieQuery.() -> Unit = {}) =
        httpExecutor.execute(
            get<PaginatedListResults<MovieListResult>>("/discover/movie") {
                DiscoveryApiMovieQuery(this).block()
            }
        )

    /**
     * Discover TV shows by different types of data like average rating, number of votes,
     * genres, the network they aired on and air dates.
     *
     * Discover also supports a nice list of sort options. See [TvQuery] for all
     * of the available options.
     *
     * Also note that a number of filters support being comma (`,`) or pipe (`|`) separated.
     * Comma's are treated like an `AND` and query while pipe's are an `OR`.
     *
     * Some examples of what can be done with discover can be found
     * [here](https://www.themoviedb.org/documentation/api/discover).
     */
    fun tv(block: TvQuery.() -> Unit = {}) =
        httpExecutor.execute(
            get<PaginatedListResults<TvListResult>>("/discover/tv") {
                DiscoveryApiTvQuery(this).block()
            }
        )

    @Suppress("TooManyFunctions")
    interface MovieQuery {
        fun language(language: LocaleCode)
        fun region(region: CountryCode)
        fun sortBy(
            option: SortOption,
            direction: SortDirection = SortDirection.Ascending
        )

        fun certificationCountry(certificationCountry: String)
        fun certification(certification: String, upperBound: Boolean = false)
        fun includeAdult()
        fun includeVideo()
        fun page(page: Int)
        fun primaryReleaseYear(year: Int)
        fun primaryReleaseDate(range: ClosedRange<LocalDate>)
        fun releaseDate(range: ClosedRange<LocalDate>)
        fun voteCount(range: IntRange)
        fun voteAverage(range: ClosedRange<Double>)

        fun withCast(personId: Int)
        fun withCrew(personId: Int)
        fun withCompany(companyId: Int)
        fun withGenre(genreId: Int)
        fun withKeyword(keywordId: Int)
        fun withPerson(personId: Int)

        fun year(year: Int)

        fun withoutGenre(genreId: Int)
        fun withoutKeyword(keywordId: Int)

        fun runtime(range: IntRange)
        fun releaseType(releaseType: String)
        fun originalLanguage(language: LanguageCode)
    }

    @Suppress("TooManyFunctions")
    interface TvQuery {
        fun language(language: LocaleCode)
        fun sortBy(
            option: SortOption,
            direction: SortDirection = SortDirection.Ascending
        )

        fun airDate(range: ClosedRange<LocalDate>)
        fun firstAirDate(range: ClosedRange<LocalDate>)
        fun firstAirDateYear(year: Int)
        fun includeNullFirstAirDates()
        fun page(page: Int)
        fun timezone(tz: TimeZone)
        fun voteCount(range: IntRange)
        fun voteAverage(range: ClosedRange<Double>)

        fun withCompany(companyId: Int)
        fun withGenre(genreId: Int)
        fun withKeyword(keywordId: Int)
        fun withNetwork(networkId: Int)

        fun withoutGenre(genreId: Int)
        fun withoutKeyword(keywordId: Int)

        fun runtime(range: IntRange)
        fun originalLanguage(language: LanguageCode)
        fun screenedTheatrically()
    }
}

@Suppress("TooManyFunctions")
private class DiscoveryApiMovieQuery(
    private val target: TmdbRequestBuilder<PaginatedListResults<MovieListResult>>
) : DiscoverApi.MovieQuery {
    override fun language(language: LocaleCode) {
        target.queryArg("language", language.toString(), replace = true)
    }

    override fun region(region: CountryCode) {
        target.queryArg("region", region.alpha2, replace = true)
    }

    override fun sortBy(option: SortOption, direction: SortDirection) {
        require(option in SortOption.movieOptions) { "Illegal sort option for movie discovery: $option" }
        target.queryArg("sort_by", "${option.value}.${direction.value}", replace = true)
    }

    override fun certificationCountry(certificationCountry: String) {
        target.queryArg("certification_country", certificationCountry, replace = true)
    }

    override fun certification(certification: String, upperBound: Boolean) {
        target.queryArg("certification${if (upperBound) ".lte" else ""}", certification, replace = true)
    }

    override fun includeAdult() {
        target.queryArg("include_adult", "true", replace = true)
    }

    override fun includeVideo() {
        target.queryArg("include_video", "true", replace = true)
    }

    override fun page(page: Int) {
        checkPage(page)
        target.queryArg("page", page, replace = true)
    }

    override fun primaryReleaseYear(year: Int) {
        target.queryArg("primary_release_year", year, replace = true)
    }

    override fun primaryReleaseDate(range: ClosedRange<LocalDate>) {
        if (range.start != LocalDate.MIN)
            target.queryArg("primary_release_date.gte", range.start.format(dateFormat), replace = true)

        if (range.endInclusive != LocalDate.MAX)
            target.queryArg("primary_release_date.lte", range.endInclusive.format(dateFormat), replace = true)
    }

    override fun releaseDate(range: ClosedRange<LocalDate>) {
        if (range.start != LocalDate.MIN)
            target.queryArg("release_date.gte", range.start.format(dateFormat), replace = true)

        if (range.endInclusive != LocalDate.MAX)
            target.queryArg("release_date.lte", range.endInclusive.format(dateFormat), replace = true)
    }

    override fun voteCount(range: IntRange) {
        if (range.first >= 0)
            target.queryArg("vote_count.gte", range.first, replace = true)

        if (range.last < Int.MAX_VALUE && range.last >= 1)
            target.queryArg("vote_count.lte", range.last, replace = true)
    }

    override fun voteAverage(range: ClosedRange<Double>) {
        if (range.start > 0.0)
            target.queryArg("vote_average.gte", range.start, replace = true)

        if (range.endInclusive < Double.MAX_VALUE && range.endInclusive >= 0.0)
            target.queryArg("vote_average.lte", range.endInclusive, replace = true)
    }

    override fun withCast(personId: Int) {
        target.queryArg("with_cast", personId)
    }

    override fun withCrew(personId: Int) {
        target.queryArg("with_crew", personId)
    }

    override fun withCompany(companyId: Int) {
        target.queryArg("with_companies", companyId)
    }

    override fun withGenre(genreId: Int) {
        target.queryArg("with_genres", genreId)
    }

    override fun withKeyword(keywordId: Int) {
        target.queryArg("with_keywords", keywordId)
    }

    override fun withPerson(personId: Int) {
        target.queryArg("with_people", personId)
    }

    override fun year(year: Int) {
        target.queryArg("year", year, replace = true)
    }

    override fun withoutGenre(genreId: Int) {
        target.queryArg("without_genres", genreId)
    }

    override fun withoutKeyword(keywordId: Int) {
        target.queryArg("without_keywords", keywordId)
    }

    override fun runtime(range: IntRange) {
        if (range.first > 0)
            target.queryArg("with_runtime.gte", range.first, replace = true)

        if (range.last < Int.MAX_VALUE)
            target.queryArg("with_runtime.lte", range.last, replace = true)
    }

    override fun releaseType(releaseType: String) {
        target.queryArg("with_release_type", releaseType, replace = true)
    }

    override fun originalLanguage(language: LanguageCode) {
        target.queryArg("with_original_language", language.toString(), replace = true)
    }
}

@Suppress("TooManyFunctions")
private class DiscoveryApiTvQuery(
    private val target: TmdbRequestBuilder<PaginatedListResults<TvListResult>>
) : DiscoverApi.TvQuery {
    override fun language(language: LocaleCode) {
        target.queryArg("language", language.toString(), replace = true)
    }

    override fun sortBy(option: SortOption, direction: SortDirection) {
        require(option in SortOption.tvOptions) { "Illegal sort option for TV discovery: $option" }
        target.queryArg("sort_by", "${option.value}.${direction.value}", replace = true)
    }

    override fun airDate(range: ClosedRange<LocalDate>) {
        if (range.start != LocalDate.MIN)
            target.queryArg("air_date.gte", range.start.format(dateFormat), replace = true)

        if (range.endInclusive != LocalDate.MAX)
            target.queryArg("air_date.lte", range.endInclusive.format(dateFormat), replace = true)
    }

    override fun firstAirDate(range: ClosedRange<LocalDate>) {
        if (range.start != LocalDate.MIN)
            target.queryArg("first_air_date.gte", range.start.format(dateFormat), replace = true)

        if (range.endInclusive != LocalDate.MAX)
            target.queryArg("first_air_date.lte", range.endInclusive.format(dateFormat), replace = true)
    }

    override fun firstAirDateYear(year: Int) {
        target.queryArg("first_air_date_year", year, replace = true)
    }

    override fun includeNullFirstAirDates() {
        target.queryArg("include_null_first_air_dates", true, replace = true)
    }

    override fun page(page: Int) {
        checkPage(page)
        target.queryArg("page", page, replace = true)
    }

    override fun timezone(tz: TimeZone) {
        target.queryArg("timezone", tz.getDisplayName(false, TimeZone.LONG, Locale.US), replace = true)
    }

    override fun voteCount(range: IntRange) {
        if (range.first >= 0)
            target.queryArg("vote_count.gte", range.first, replace = true)

        if (range.last < Int.MAX_VALUE && range.last >= 1)
            target.queryArg("vote_count.lte", range.last, replace = true)
    }

    override fun voteAverage(range: ClosedRange<Double>) {
        if (range.start >= 0.0)
            target.queryArg("vote_average.gte", range.start, replace = true)

        if (range.endInclusive < Double.MAX_VALUE && range.endInclusive >= 0.0)
            target.queryArg("vote_average.lte", range.endInclusive, replace = true)
    }

    override fun withCompany(companyId: Int) {
        target.queryArg("with_companies", companyId)
    }

    override fun withGenre(genreId: Int) {
        target.queryArg("with_genres", genreId)
    }

    override fun withKeyword(keywordId: Int) {
        target.queryArg("with_keywords", keywordId)
    }

    override fun withNetwork(networkId: Int) {
        target.queryArg("with_networks", networkId)
    }

    override fun withoutGenre(genreId: Int) {
        target.queryArg("without_genres", genreId)
    }

    override fun withoutKeyword(keywordId: Int) {
        target.queryArg("without_keywords", keywordId)
    }

    override fun runtime(range: IntRange) {
        if (range.first > 0)
            target.queryArg("with_runtime.gte", range.first, replace = true)

        if (range.last < Int.MAX_VALUE)
            target.queryArg("with_runtime.lte", range.last, replace = true)
    }

    override fun originalLanguage(language: LanguageCode) {
        target.queryArg("with_original_language", language.toString(), replace = true)
    }

    override fun screenedTheatrically() {
        target.queryArg("screened_theatrically", true, replace = true)
    }
}
