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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreditsTest : AbstractTmdbTest() {
    companion object {
        const val TV_CREDIT_ID = "5bf57dad9251417c1d047265"
        const val TV_MEDIA_ID = 1403
        const val TV_CHARACTER = """Elena 'Yo-Yo' Rodriguez / Slingshot"""

        const val MOVIE_CREDIT_ID = "545d46a80e0a261fb3004e81"
        const val MOVIE_MEDIA_ID = 284053
        const val MOVIE_CHARACTER = "Thor Odinson"
    }

    @Test
    fun testGetDetailsForTv() {
        val credits = client.credits.details(TV_CREDIT_ID).blockingFirst()

        assertEquals(TV_MEDIA_ID, credits.media?.id)
        assertEquals(TV_CHARACTER, credits.media?.character)
    }

    @Test
    fun testGetDetailsForMovie() {
        val credits = client.credits.details(MOVIE_CREDIT_ID).blockingFirst()

        assertEquals(MOVIE_MEDIA_ID, credits.media?.id)
        assertEquals(MOVIE_CHARACTER, credits.media?.character)
    }
}
