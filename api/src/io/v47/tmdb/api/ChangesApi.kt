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

import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Change
import io.v47.tmdb.model.PaginatedListResults
import java.time.LocalDate

class ChangesApi internal constructor(private val httpExecutor: HttpExecutor) {
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
        httpExecutor.execute(
            getWithPage<PaginatedListResults<Change>>("/$type/changes", page) {
                endDate?.let { queryArg("end_date", it) }
                startDate?.let { queryArg("start_date", it) }
            }
        )
}
