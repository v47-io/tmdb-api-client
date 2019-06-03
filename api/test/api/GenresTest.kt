package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GenresTest : AbstractTmdbTest() {
    @Test
    fun testGetMovieList() {
        val result = client.genres.forMovies().blockingFirst()
        assertTrue(result.genres.isNotEmpty())
    }

    @Test
    fun testGetTvList() {
        val result = client.genres.forTv().blockingFirst()
        assertTrue(result.genres.isNotEmpty())
    }
}
