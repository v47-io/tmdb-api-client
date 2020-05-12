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

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SearchTest : AbstractTmdbTest() {
    companion object {
        const val COMPANY_QUERY = "walt disney"
        const val WALT_DISNEY_PICTURES_ID = 2

        const val COLLECTION_QUERY = "the avengers"
        const val THE_AVENGERS_COLLECTION_ID = 86311

        const val KEYWORD_QUERY = "slasher"
        const val PROTOSLASHER_ID = 233450

        const val MOVIE_QUERY = "star wars"
        const val MOVIE_QUERY_YEAR = 2015
        const val SW_TFA_ID = 140607

        const val MULTI_TV_QUERY = "agents of shield"
        const val AGENTS_OF_SHIELD_ID = 1403

        const val PERSON_QUERY = "clark gregg"
        const val CLARK_GREGG_ID = 9048
    }

    private val search by lazy { client.search }

    @Test
    fun testSearchCompany() {
        val results = search.forCompanies(COMPANY_QUERY).blockingFirst()
        assertNotNull(results.results.find { it.id == WALT_DISNEY_PICTURES_ID })
    }

    @Test
    fun testSearchCollections() {
        val results = search.forCollections(COLLECTION_QUERY).blockingFirst()
        assertNotNull(results.results.find { it.id == THE_AVENGERS_COLLECTION_ID })
    }

    @Test
    fun testSearchKeywords() {
        val results = search.forKeywords(KEYWORD_QUERY).blockingFirst()
        assertNotNull(results.results.find { it.id == PROTOSLASHER_ID })
    }

    @Test
    fun testSearchMovies() {
        val results = search.forMovies(MOVIE_QUERY, year = MOVIE_QUERY_YEAR).blockingFirst()
        assertNotNull(results.results.find { it.id == SW_TFA_ID })
    }

    @Test
    fun testSearchMulti() {
        val results = search.forVarious(MULTI_TV_QUERY).blockingFirst()
        assertNotNull(results.results.find { it.id == AGENTS_OF_SHIELD_ID })
    }

    @Test
    fun testSearchPeople() {
        val results = search.forPeople(PERSON_QUERY).blockingFirst()
        assertNotNull(results.results.find { it.id == CLARK_GREGG_ID })
    }

    @Test
    fun testSearchTvShows() {
        val results = search.forTvShows(MULTI_TV_QUERY).blockingFirst()
        assertNotNull(results.results.find { it.id == AGENTS_OF_SHIELD_ID })
    }
}
