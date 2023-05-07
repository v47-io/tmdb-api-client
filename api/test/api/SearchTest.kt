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

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SearchTest : AbstractTmdbTest() {
    companion object {
        const val COMPANY_QUERY = "walt disney pictures"
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
