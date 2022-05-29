/**
 * Copyright 2022 The tmdb-api-v2 Authors
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

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ChangesTest : AbstractTmdbTest() {
    private val startDate = LocalDate.now().minusDays(10)
    private val endDate = LocalDate.now().minusDays(3)
    private val page = 1

    private val changes by lazy { client.changes }

    @Test
    fun testGetMovieChangeList() {
        val result = changes.forMovies().blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetPersonChangeList() {
        val result = changes.forPeople().blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetTvChangeList() {
        val result = changes.forTv().blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetMovieChangeListWithArguments() {
        val result = changes.forMovies(endDate, startDate, page).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetPersonChangeListWithArguments() {
        val result = changes.forPeople(endDate, startDate, page).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetTvChangeListWithArguments() {
        val result = changes.forTv(endDate, startDate, page).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }
}
