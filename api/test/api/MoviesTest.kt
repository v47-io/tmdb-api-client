package io.v47.tmdb.api

import com.neovisionaries.i18n.CountryCode
import io.v47.tmdb.model.Release
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MoviesTest : AbstractTmdbTest() {
    companion object {
        const val THOR_RAGNAROK_ID = 284053
        const val THOR_RUSSIAN_TITLE = "Тор 3: Рагнарёк" // it's getting worse
        const val CAST_LOKI_CREDIT_ID = "545d46c7c3a3686cbb00002d"
        const val CAST_LOKI_ACTOR_NAME = "Tom Hiddleston"
        const val THOR_RAGNAROK_IMDB_ID = "tt3501632"
        const val KEYWORD_NORSE_GOD_ID = 230105
        val US_THEATRICAL_RELEASE_DATE = LocalDate.parse("2017-11-03")!!
        const val LATVIAN_TRANSLATION_NAME = "Latviešu"
    }

    private val movie by lazy { client.movie }

    @Test
    fun testGetDetailsWithTitles() {
        val details = movie.details(
            THOR_RAGNAROK_ID,
            append = *arrayOf(MovieRequest.AlternativeTitles)
        ).blockingFirst()

        assertEquals(THOR_RAGNAROK_ID, details.id)

        assertNotNull(details.alternativeTitles)
        assertEquals(
            THOR_RUSSIAN_TITLE,
            details.alternativeTitles!!.titles.find { it.country == CountryCode.RU }?.title
        )
    }

    @Test
    fun testGetAlternativeTitles() {
        val altTitles = movie.alternativeTitles(
            THOR_RAGNAROK_ID,
            CountryCode.RU
        ).blockingFirst()

        assertEquals(1, altTitles.titles.size)
        assertEquals(THOR_RUSSIAN_TITLE, altTitles.titles[0].title)
    }

    @Test
    fun testGetChanges() {
        movie.changes(THOR_RAGNAROK_ID).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        val credits = movie.credits(THOR_RAGNAROK_ID).blockingFirst()

        assertEquals(
            CAST_LOKI_ACTOR_NAME,
            credits.cast.find { it.creditId == CAST_LOKI_CREDIT_ID }?.name
        )
    }

    @Test
    fun testGetExternalIds() {
        val extIds = movie.externalIds(THOR_RAGNAROK_ID).blockingFirst()

        assertEquals(THOR_RAGNAROK_IMDB_ID, extIds.imdbId)
    }

    @Test
    fun testGetImages() {
        movie.images(THOR_RAGNAROK_ID).blockingFirst()
    }

    @Test
    fun testGetKeywords() {
        val keywords = movie.keywords(THOR_RAGNAROK_ID).blockingFirst()

        assertNotNull(keywords.keywords.find { it.id == KEYWORD_NORSE_GOD_ID })
    }

    @Test
    fun testGetReleaseDates() {
        val releaseDates = movie.releaseDates(THOR_RAGNAROK_ID).blockingFirst()

        assertEquals(
            US_THEATRICAL_RELEASE_DATE,
            releaseDates.results
                .find { it.country == CountryCode.US }
                ?.releaseDates
                ?.find { it.type == Release.Theatrical }
                ?.releaseDate
        )
    }

    @Test
    fun testGetVideos() {
        val videos = movie.videos(THOR_RAGNAROK_ID).blockingFirst()

        assertTrue(videos.results.isNotEmpty())
    }

    @Test
    fun testGetTranslations() {
        val translations = movie.translations(THOR_RAGNAROK_ID).blockingFirst()

        assertEquals(
            LATVIAN_TRANSLATION_NAME,
            translations.translations.find { it.country == CountryCode.LV }?.name
        )
    }

    @Test
    fun testGetRecommendations() {
        val recommendations = movie.recommendations(THOR_RAGNAROK_ID).blockingFirst()

        assertTrue(recommendations.results.isNotEmpty())
    }

    @Test
    fun testGetSimilar() {
        val similar = movie.similar(THOR_RAGNAROK_ID).blockingFirst()

        assertTrue(similar.results.isNotEmpty())
    }

    @Test
    fun testGetReviews() {
        val reviews = movie.reviews(THOR_RAGNAROK_ID).blockingFirst()

        assertTrue(reviews.results.isNotEmpty())
    }

    @Test
    fun testGetLists() {
        val lists = movie.lists(THOR_RAGNAROK_ID).blockingFirst()

        assertTrue(lists.results.isNotEmpty())
    }

    @Test
    fun testGetLatest() {
        movie.latest().blockingFirst()
    }

    @Test
    fun testGetNowPlaying() {
        movie.nowPlaying().blockingFirst()
    }

    @Test
    fun testGetPopular() {
        movie.popular().blockingFirst()
    }

    @Test
    fun testGetTopRated() {
        movie.topRated().blockingFirst()
    }

    @Test
    fun testGetUpcoming() {
        movie.upcoming().blockingFirst()
    }
}
