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

import io.v47.tmdb.model.Session
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

// only enable locally
@Disabled("only test locally when needed, otherwise rate limits apply")
class GuestSessionTest : AbstractTmdbTest() {
    companion object {
        const val KUNG_FU_HUSTLE_ID = 9470
        const val THOR_RAGNAROK_ID = 284053

        const val AGENTS_OF_SHIELD_ID = 1403
    }

    private lateinit var session: Session.Guest

    @BeforeAll
    fun init() {
        session =
            Session.Guest(
                client
                    .authentication
                    .createGuestSession()
                    .blockingFirst()
                    .id
            )

        client.movie.rate(KUNG_FU_HUSTLE_ID, 9.0, session).blockingFirst()
        client.movie.rate(THOR_RAGNAROK_ID, 7.5, session).blockingFirst()

        client.tvShow.rate(AGENTS_OF_SHIELD_ID, 7.5, session).blockingFirst()

        client.tvEpisode.rate(AGENTS_OF_SHIELD_ID, 1, 1, 8.5, session).blockingFirst()
        client.tvEpisode.rate(AGENTS_OF_SHIELD_ID, 1, 2, 8.5, session).blockingFirst()
        client.tvEpisode.rate(AGENTS_OF_SHIELD_ID, 1, 3, 8.5, session).blockingFirst()

        // need to wait until the ratings propagate across TMDB systems
        Thread.sleep(1000)
    }

    @Test
    fun testGetRatedMovies() {
        val ratedMovies = client.guestSession.getRatedMovies(session).blockingFirst()

        assertEquals(2, ratedMovies.results.size)
    }

    @Test
    fun testGetRatedTvShows() {
        val ratedTvShows = client.guestSession.getRatedTvShows(session).blockingFirst()

        assertEquals(1, ratedTvShows.results.size)
    }

    @Test
    fun testGetRatedTvEpisodes() {
        val ratedTvEpisodes = client.guestSession.getRatedTvEpisodes(session).blockingFirst()

        assertEquals(3, ratedTvEpisodes.results.size)
    }

    @AfterAll
    fun cleanup() {
        runCatching {
            client.movie.removeRating(KUNG_FU_HUSTLE_ID, session).blockingFirst()
        }

        runCatching {
            client.movie.removeRating(THOR_RAGNAROK_ID, session).blockingFirst()
        }

        runCatching {
            client.tvShow.removeRating(AGENTS_OF_SHIELD_ID, session).blockingFirst()
        }

        runCatching {
            client.tvEpisode.removeRating(AGENTS_OF_SHIELD_ID, 1, 1, session).blockingFirst()
        }

        runCatching {
            client.tvEpisode.removeRating(AGENTS_OF_SHIELD_ID, 1, 2, session).blockingFirst()
        }

        runCatching {
            client.tvEpisode.removeRating(AGENTS_OF_SHIELD_ID, 1, 3, session).blockingFirst()
        }
    }
}
