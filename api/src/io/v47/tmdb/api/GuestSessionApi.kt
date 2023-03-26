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
import io.v47.tmdb.http.TmdbRequestBuilder
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.MovieListResult
import io.v47.tmdb.model.PaginatedListResults
import io.v47.tmdb.model.Session
import io.v47.tmdb.model.TvEpisodeListResult
import io.v47.tmdb.model.TvListResult

class GuestSessionApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get the rated movies for a guest session.
     *
     * @param sortDirection Sort the results ([SortOption.CreatedAt])
     */
    fun getRatedMovies(
        session: Session.Guest,
        page: Int? = null,
        language: LocaleCode? = null,
        sortDirection: SortDirection? = null
    ) =
        http.getWithPageAndLanguage<PaginatedListResults<MovieListResult>>(
            "/guest_session/{guestSessionId}/rated/movies",
            page,
            language,
            configure(session.id, sortDirection)
        )

    /**
     * Get the rated TV shows for a guest session.
     *
     * @param sortDirection Sort the results ([SortOption.CreatedAt])
     */
    fun getRatedTvShows(
        session: Session.Guest,
        page: Int? = null,
        language: LocaleCode? = null,
        sortDirection: SortDirection? = null
    ) =
        http.getWithPageAndLanguage<PaginatedListResults<TvListResult>>(
            "/guest_session/{guestSessionId}/rated/tv",
            page,
            language,
            configure(session.id, sortDirection)
        )

    /**
     * Get the rated TV episodes for a guest session.
     *
     * @param sortDirection Sort the results ([SortOption.CreatedAt])
     */
    fun getRatedTvEpisodes(
        session: Session.Guest,
        page: Int? = null,
        language: LocaleCode? = null,
        sortDirection: SortDirection? = null
    ) =
        http.getWithPageAndLanguage<PaginatedListResults<TvEpisodeListResult>>(
            "/guest_session/{guestSessionId}/rated/tv/episodes",
            page,
            language,
            configure(session.id, sortDirection)
        )

    private fun <T : Any> configure(
        guestSessionId: String,
        sortDirection: SortDirection?
    ) =
        fun TmdbRequestBuilder<T>.() {
            pathVar("guestSessionId", guestSessionId)

            if (sortDirection != null)
                queryArg("sort_by", "${SortOption.CreatedAt.value}.${sortDirection.value}")
        }
}
