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
