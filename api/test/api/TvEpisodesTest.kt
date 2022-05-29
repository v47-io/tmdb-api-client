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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class TvEpisodesTest : AbstractTmdbTest() {
    companion object {
        const val AGENTS_OF_SHIELD_ID = 1403
        const val SEASON = 5
        const val EPISODE = 7

        const val S05E07_ID = 1408720
    }

    private val tvEpisode by lazy { client.tvEpisode }

    @Test
    fun testGetDetailsAndImages() {
        val results = tvEpisode.details(
            AGENTS_OF_SHIELD_ID,
            SEASON,
            EPISODE,
            append = *TvEpisodeRequest.values()
        ).blockingFirst()

        assertNotNull(results.images)
    }

    @Test
    fun testGetChanges() {
        tvEpisode.changes(S05E07_ID).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        tvEpisode.credits(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetExternalIds() {
        tvEpisode.externalIds(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetImages() {
        tvEpisode.images(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetTranslations() {
        tvEpisode.translations(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetVideos() {
        tvEpisode.videos(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }
}
