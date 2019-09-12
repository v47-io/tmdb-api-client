package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreditsTest : AbstractTmdbTest() {
    companion object {
        const val TV_CREDIT_ID = "5a1a319a0e0a264ccd036b53"
        const val TV_MEDIA_ID = 1403
        const val TV_CHARACTER = "Elena \u201cYo-Yo\u201d Rodriguez / Slingshot"

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
