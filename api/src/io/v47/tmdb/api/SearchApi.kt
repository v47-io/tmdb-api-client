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
package io.v47.tmdb.api

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.*
import org.reactivestreams.Publisher

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
    ): Publisher<out Paginated<MovieTvPersonListResult>> =
        httpExecutor.execute(
            getWithPageAndLanguage<PaginatedMovieTvPersonListResults>("/search/multi", page, language) {
                queryArg("query", query)

                region?.let { queryArg("region", it.toString()) }
                includeAdult?.let { queryArg("include_adult", it) }
            }
        )

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
