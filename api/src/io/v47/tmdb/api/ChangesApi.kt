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

import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Change
import io.v47.tmdb.model.PaginatedListResults
import java.time.LocalDate

class ChangesApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get a list of all of the movie ids that have been changed in the past 24 hours.
     *
     * You can query it for up to 14 days worth of changed IDs at a time with the
     * `start_date` and `end_date` query parameters. 100 items are returned per page
     *
     * @param endDate The end of the time range for the results
     * @param startDate The start of the time range for the result
     * @param page The page number for the selected result
     */
    fun forMovies(endDate: LocalDate? = null, startDate: LocalDate? = null, page: Int? = null) =
        getChangeList("movie", endDate, startDate, page)

    /**
     * Get a list of all of the TV show ids that have been changed in the past 24 hours.
     *
     * You can query it for up to 14 days worth of changed IDs at a time with the
     * `start_date` and `end_date` query parameters. 100 items are returned per page
     *
     * @param endDate The end of the time range for the results
     * @param startDate The start of the time range for the result
     * @param page The page number for the selected result
     */
    fun forTv(endDate: LocalDate? = null, startDate: LocalDate? = null, page: Int? = null) =
        getChangeList("tv", endDate, startDate, page)

    /**
     * Get a list of all of the person ids that have been changed in the past 24 hours.
     *
     * You can query it for up to 14 days worth of changed IDs at a time with the
     * `start_date` and `end_date` query parameters. 100 items are returned per page
     *
     * @param endDate The end of the time range for the results
     * @param startDate The start of the time range for the result
     * @param page The page number for the selected result
     */
    fun forPeople(endDate: LocalDate? = null, startDate: LocalDate? = null, page: Int? = null) =
        getChangeList("person", endDate, startDate, page)

    private fun getChangeList(
        type: String,
        endDate: LocalDate? = null,
        startDate: LocalDate? = null,
        page: Int? = null
    ) =
        http.getWithPage<PaginatedListResults<Change>>("/$type/changes", page) {
            endDate?.let { queryArg("end_date", it) }
            startDate?.let { queryArg("start_date", it) }
        }

}
