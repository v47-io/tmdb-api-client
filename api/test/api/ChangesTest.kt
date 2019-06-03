package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ChangesTest : AbstractTmdbTest() {
    private val startDate = LocalDate.now().minusDays(10)
    private val endDate = LocalDate.now().minusDays(3)
    private val page = 1

    private val changes by lazy { client.changes }

    @Test
    fun testGetMovieChangeList() {
        val result = changes.forMovies().blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetPersonChangeList() {
        val result = changes.forPeople().blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetTvChangeList() {
        val result = changes.forTv().blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetMovieChangeListWithArguments() {
        val result = changes.forMovies(endDate, startDate, page).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetPersonChangeListWithArguments() {
        val result = changes.forPeople(endDate, startDate, page).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }

    @Test
    fun testGetTvChangeListWithArguments() {
        val result = changes.forTv(endDate, startDate, page).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }
}
