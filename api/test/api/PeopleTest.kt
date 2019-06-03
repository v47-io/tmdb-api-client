package io.v47.tmdb.api

import io.reactivex.Flowable
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PeopleTest : AbstractTmdbTest() {
    companion object {
        const val CHRIS_HEMSWORTH_ID = 74568
        const val STAR_TREK_CREDIT_ID = "52fe456c9251416c7505619d"
        const val CHRIS_HEMSWORTH_IMDB_ID = "nm1165110"
    }

    private val person by lazy { client.person }

    @Test
    fun testGetDetailsWithCombinedCredits() {
        val details = Flowable.fromPublisher(
            person.details(
                CHRIS_HEMSWORTH_ID,
                append = *arrayOf(PeopleRequest.CombinedCredits)
            )
        ).blockingFirst()

        assertEquals(CHRIS_HEMSWORTH_ID, details.id)

        assertNotNull(details.combinedCredits)
        assertNotNull(details.combinedCredits!!.cast.find { it.creditId == STAR_TREK_CREDIT_ID })
    }

    @Test
    fun testGetMovieCredits() {
        val credits = person.movieCredits(CHRIS_HEMSWORTH_ID).blockingFirst()
        assertNotNull(credits.cast.find { it.creditId == STAR_TREK_CREDIT_ID })
    }

    @Test
    fun testGetTvCredits() {
        assertTrue(person.tvCredits(CHRIS_HEMSWORTH_ID).blockingFirst().cast.isNotEmpty())
    }

    @Test
    fun testGetCombinedCredits() {
        val credits = person.combinedCredits(CHRIS_HEMSWORTH_ID).blockingFirst()
        assertNotNull(credits.cast.find { it.creditId == STAR_TREK_CREDIT_ID })
    }

    @Test
    fun testGetExternalIds() {
        assertEquals(
            CHRIS_HEMSWORTH_IMDB_ID,
            person.externalIds(CHRIS_HEMSWORTH_ID).blockingFirst().imdbId
        )
    }

    @Test
    fun testGetImages() {
        assertTrue(person.images(CHRIS_HEMSWORTH_ID).blockingFirst().profiles.isNotEmpty())
    }

    @Test
    fun testGetTaggedImages() {
        assertTrue(
            Flowable
                .fromPublisher(person.taggedImages(CHRIS_HEMSWORTH_ID))
                .blockingFirst()
                .results
                .isNotEmpty()
        )
    }

    @Test
    fun testGetTranslations() {
        assertTrue(
            Flowable
                .fromPublisher(person.translations(CHRIS_HEMSWORTH_ID))
                .blockingFirst()
                .translations
                .isNotEmpty()
        )
    }

    @Test
    fun testGetChanges() {
        person.changes(CHRIS_HEMSWORTH_ID).blockingFirst()
    }

    @Test
    fun testGetLatest() {
        person.latest().blockingFirst()
    }

    @Test
    fun testGetPopular() {
        person.popular().blockingFirst()
    }
}