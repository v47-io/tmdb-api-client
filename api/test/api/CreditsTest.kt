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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreditsTest : AbstractTmdbTest() {
    companion object {
        const val TV_CREDIT_ID = "5bf57dad9251417c1d047265"
        const val TV_MEDIA_ID = 1403
        const val TV_CHARACTER = """Elena "Yo-Yo" Rodriguez / Slingshot"""

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
