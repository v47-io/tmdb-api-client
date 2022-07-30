/**
 * The Clear BSD License
 *
 * Copyright (c) 2022 the tmdb-api-client authors
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
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TvTest : AbstractTmdbTest() {
    companion object {
        const val AGENTS_OF_SHIELD_ID = 1403
        const val AGENTS_OF_SHIELD_NAME_ES = "Marvel Agentes de S.H.I.E.L.D."

        const val GAME_OF_THRONES_ID = 1399
        const val SUPERGIRL_ID = 62688
    }

    private val tvShow by lazy { client.tvShow }

    @Test
    fun testGetDetailsAndTranslationsVideos() {
        val details = tvShow.details(
            AGENTS_OF_SHIELD_ID,
            language = LocaleCode.es_ES,
            append = TvRequest.values()
        ).blockingFirst()

        assertEquals(AGENTS_OF_SHIELD_NAME_ES, details.name)
        assertNotNull(details.translations)
        assertNotNull(details.videos)
    }

    @Test
    fun testGetAlternativeTitles() {
        tvShow.alternativeTitles(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetChanges() {
        tvShow.changes(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetContentRatings() {
        tvShow.contentRatings(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        tvShow.credits(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetEpisodeGroups() {
        val episodeGroups = tvShow.episodeGroups(SUPERGIRL_ID).blockingFirst()
        assertTrue(episodeGroups.results.isNotEmpty())
    }

    @Test
    fun testGetExternalIds() {
        tvShow.externalIds(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetImages() {
        tvShow.images(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetKeywords() {
        tvShow.keywords(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetRecommendations() {
        tvShow.recommendations(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetReviews() {
        tvShow.reviews(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetScreenedTheatrically() {
        tvShow.screenedTheatrically(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetSimilar() {
        tvShow.similar(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetTranslations() {
        tvShow.translations(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetVideos() {
        tvShow.videos(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetLatest() {
        tvShow.latest().blockingFirst()
    }

    @Test
    fun testGetAiringToday() {
        tvShow.getAiringToday().blockingFirst()
    }

    @Test
    fun testGetOnTheAir() {
        tvShow.getOnTheAir().blockingFirst()
    }

    @Test
    fun testGetPopular() {
        tvShow.getPopular().blockingFirst()
    }

    @Test
    fun testGetTopRated() {
        tvShow.getTopRated().blockingFirst()
    }
}
