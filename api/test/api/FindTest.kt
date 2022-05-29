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

class FindTest : AbstractTmdbTest() {
    companion object {
        const val AVENGERS_IMDB_ID = "tt0848228"
        const val AGENTS_OF_SHIELD_IMDB_ID = "tt2364582"
        const val SHIELD_SEASON_3_TVDB_ID = "625697"
        const val SHIELD_S05E01_IMDB_ID = "tt6878538"
        const val CLARK_GREGG_IMDB_ID = "nm0163988"
    }

    private val find by lazy { client.find }

    @Test
    fun testFindMovies() {
        val result = find.byId(AVENGERS_IMDB_ID, FindApi.ExternalSource.IMDb).blockingFirst()
        assertTrue(result.movieResults.isNotEmpty())
    }

    @Test
    fun testFindTv() {
        val result = find.byId(
            AGENTS_OF_SHIELD_IMDB_ID,
            FindApi.ExternalSource.IMDb
        ).blockingFirst()
        assertTrue(result.tvResults.isNotEmpty())
    }

    @Test
    fun testFindSeason() {
        val result = find.byId(
            SHIELD_SEASON_3_TVDB_ID,
            FindApi.ExternalSource.TVDB
        ).blockingFirst()
        assertTrue(result.tvSeasonResults.isNotEmpty())
    }

    @Test
    fun testFindEpisode() {
        val result = find.byId(
            SHIELD_S05E01_IMDB_ID,
            FindApi.ExternalSource.IMDb
        ).blockingFirst()
        assertTrue(result.tvEpisodeResults.isNotEmpty())
    }

    @Test
    fun testFindPerson() {
        val result = find.byId(CLARK_GREGG_IMDB_ID, FindApi.ExternalSource.IMDb).blockingFirst()
        assertTrue(result.personResults.isNotEmpty())
    }
}
