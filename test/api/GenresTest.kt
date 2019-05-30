package io.v47.tmdb.api

import io.reactivex.Flowable
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GenresTest : AbstractTmdbTest() {
    @Test
    fun testGetMovieList() {
        val result = Flowable.fromPublisher(client.genres.forMovies()).blockingFirst()
        assertTrue(result.genres.isNotEmpty())
    }

    @Test
    fun testGetTvList() {
        val result = Flowable.fromPublisher(client.genres.forTv()).blockingFirst()
        assertTrue(result.genres.isNotEmpty())
    }
}
