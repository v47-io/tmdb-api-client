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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class PeopleTest : AbstractTmdbTest() {
    companion object {
        const val CHRIS_HEMSWORTH_ID = 74568
        const val STAR_TREK_CREDIT_ID = "52fe456c9251416c7505619d"
        const val CHRIS_HEMSWORTH_IMDB_ID = "nm1165110"
    }

    private val person by lazy { client.person }

    @Test
    fun testGetDetailsWithCombinedCredits() {
        val details =
            person.details(
                CHRIS_HEMSWORTH_ID,
                append = PeopleRequest.entries.toTypedArray()
            ).blockingFirst()

        assertEquals(CHRIS_HEMSWORTH_ID, details.id)

        assertNotNull(details.combinedCredits)
        assertNotNull(details.combinedCredits!!.cast.find { it.creditId == STAR_TREK_CREDIT_ID })
    }

    @Test
    fun testGetMovieCredits() {
        val credits = person.movieCredits(CHRIS_HEMSWORTH_ID).blockingFirst()
        assertNotNull(credits.cast.find { it.creditId == STAR_TREK_CREDIT_ID })
    }

    @Test
    fun testGetTvCredits() {
        assertTrue(person.tvCredits(CHRIS_HEMSWORTH_ID).blockingFirst().cast.isNotEmpty())
    }

    @Test
    fun testGetCombinedCredits() {
        val credits = person.combinedCredits(CHRIS_HEMSWORTH_ID).blockingFirst()
        assertNotNull(credits.cast.find { it.creditId == STAR_TREK_CREDIT_ID })
    }

    @Test
    fun testGetExternalIds() {
        assertEquals(
            CHRIS_HEMSWORTH_IMDB_ID,
            person.externalIds(CHRIS_HEMSWORTH_ID).blockingFirst().imdbId
        )
    }

    @Test
    fun testGetImages() {
        assertTrue(person.images(CHRIS_HEMSWORTH_ID).blockingFirst().profiles.isNotEmpty())
    }

    @Test
    @Disabled("Endpoint is deprecated in TMDB.")
    fun testGetTaggedImages() {
        assertTrue(
            @Suppress("DEPRECATION")
            person.taggedImages(CHRIS_HEMSWORTH_ID)
                .blockingFirst()
                .results
                .isNotEmpty()
        )
    }

    @Test
    fun testGetTranslations() {
        assertTrue(
            person.translations(CHRIS_HEMSWORTH_ID)
                .blockingFirst()
                .translations
                .isNotEmpty()
        )
    }

    @Test
    fun testGetChanges() {
        person.changes(CHRIS_HEMSWORTH_ID).blockingFirst()
    }

    @Test
    fun testGetLatest() {
        person.latest().blockingFirst()
    }

    @Test
    fun testGetPopular() {
        person.popular().blockingFirst()
    }
}
