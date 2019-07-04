package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KeywordsTest : AbstractTmdbTest() {
    companion object {
        const val KEYWORD_ID = 1553
        const val KEYWORD_NAME = "pottery"
    }

    @Test
    fun testGetKeyword() {
        val result = client.keyword.details(KEYWORD_ID).blockingFirst()
        assertEquals(KEYWORD_NAME, result.name)
    }

    @Test
    fun getKeywordMovies() {
        val result = client.keyword.movies(KEYWORD_ID).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }
}