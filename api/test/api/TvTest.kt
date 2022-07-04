/**
 * Copyright 2022 The tmdb-api-client Authors
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
