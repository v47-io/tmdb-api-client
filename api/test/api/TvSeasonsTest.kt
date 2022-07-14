/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TvSeasonsTest : AbstractTmdbTest() {
    companion object {
        const val AGENTS_OF_SHIELD_ID = 1403
        const val SEASON = 5

        const val SEASON_ID = 93742
    }

    private val tvSeason by lazy { client.tvSeason }

    @Test
    fun testGetDetailsAndCredits() {
        val details = tvSeason.details(
            AGENTS_OF_SHIELD_ID,
            SEASON,
            append = TvSeasonRequest.values()
        ).blockingFirst()

        Assertions.assertNotNull(details.credits)
    }

    @Test
    fun testGetChanges() {
        tvSeason.changes(SEASON_ID).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        tvSeason.credits(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }

    @Test
    fun testGetExternalIds() {
        tvSeason.externalIds(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }

    @Test
    fun testGetImages() {
        tvSeason.images(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }

    @Test
    fun testGetVideos() {
        tvSeason.videos(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }
}