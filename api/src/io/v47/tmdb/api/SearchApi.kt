package io.v47.tmdb.api

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.*

class SearchApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Search for companies
     *
     * @param query Pass a text query to search
     * @param page Specify which page to query
     */
    fun forCompanies(query: String, page: Int? = null) =
        httpExecutor.execute(
            getWithPage<PaginatedListResults<CompanyInfo>>("/search/company", page) {
                queryArg("query", query)
            }
        )

    /**
     * Search for collections
     *
     * @param query Pass a text query to search
     * @param language A language code
     * @param page Specify which page to query
     */
    fun forCollections(query: String, page: Int? = null, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithPageAndLanguage<PaginatedListResults<CollectionInfo>>("/search/collection", page, language) {
                queryArg("query", query)
            }
        )

    /**
     * Search for keywords
     *
     * @param query Pass a text query to search
     * @param page Specify which page to query
     */
    fun forKeywords(query: String, page: Int? = null) =
        httpExecutor.execute(
            getWithPage<PaginatedListResults<Keyword>>("/search/keyword", page) {
                queryArg("query", query)
            }
        )

    /**
     * Search for movies
     *
     * @param query Pass a text query to search
     */
    @Suppress("LongParameterList")
    fun forMovies(
        query: String,
        page: Int? = null,
        language: LocaleCode? = null,
        region: CountryCode? = null,
        includeAdult: Boolean? = null,
        year: Int? = null,
        primaryReleaseYear: Int? = null
    ) = httpExecutor.execute(
        getWithPageAndLanguage<PaginatedListResults<MovieListResult>>("/search/movie", page, language) {
            queryArg("query", query)

            region?.let { queryArg("region", it.toString()) }
            includeAdult?.let { queryArg("include_adult", it) }
            year?.let { queryArg("year", it) }
            primaryReleaseYear?.let { queryArg("primary_release_year", it) }
        }
    )

    @Suppress("UNCHECKED_CAST")
    fun forVarious(
        query: String,
        page: Int? = null,
        language: LocaleCode? = null,
        region: CountryCode? = null,
        includeAdult: Boolean? = null
    ): Paginated<MovieTvPersonListResult> =
        httpExecutor.execute(
            getWithPageAndLanguage<PaginatedMovieTvPersonListResults>("/search/multi", page, language) {
                queryArg("query", query)

                region?.let { queryArg("region", it.toString()) }
                includeAdult?.let { queryArg("include_adult", it) }
            }
        ) as Paginated<MovieTvPersonListResult>

    fun forPeople(
        query: String,
        page: Int? = null,
        language: LocaleCode? = null,
        region: CountryCode? = null,
        includeAdult: Boolean? = null
    ) = httpExecutor.execute(
        getWithPageAndLanguage<PaginatedListResults<PersonListResult>>("/search/person", page, language) {
            queryArg("query", query)

            region?.let { queryArg("region", it.toString()) }
            includeAdult?.let { queryArg("include_adult", it) }
        }
    )

    fun forTvShows(
        query: String,
        page: Int? = null,
        language: LocaleCode? = null,
        firstAirDateYear: Int? = null
    ) = httpExecutor.execute(
        getWithPageAndLanguage<PaginatedListResults<TvListResult>>("/search/tv", page, language) {
            queryArg("query", query)

            firstAirDateYear?.let { queryArg("first_air_date_year", it) }
        }
    )
}
