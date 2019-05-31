package io.v47.tmdb.api

import com.neovisionaries.i18n.CountryCode
import io.reactivex.Flowable
import io.v47.tmdb.model.Release
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
        val details = Flowable.fromPublisher(
            movie.details(
                THOR_RAGNAROK_ID,
                append = *arrayOf(MovieRequest.AlternativeTitles)
            )
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
        val altTitles = Flowable.fromPublisher(
            movie.alternativeTitles(
                THOR_RAGNAROK_ID,
                CountryCode.RU
            )
        ).blockingFirst()

        assertEquals(1, altTitles.titles.size)
        assertEquals(THOR_RUSSIAN_TITLE, altTitles.titles[0].title)
    }

    @Test
    fun testGetChanges() {
        Flowable.fromPublisher(movie.changes(THOR_RAGNAROK_ID)).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        val credits = Flowable.fromPublisher(movie.credits(THOR_RAGNAROK_ID)).blockingFirst()

        assertEquals(
            CAST_LOKI_ACTOR_NAME,
            credits.cast.find { it.creditId == CAST_LOKI_CREDIT_ID }?.name
        )
    }

    @Test
    fun testGetExternalIds() {
        val extIds = Flowable.fromPublisher(movie.externalIds(THOR_RAGNAROK_ID)).blockingFirst()

        assertEquals(THOR_RAGNAROK_IMDB_ID, extIds.imdbId)
    }

    @Test
    fun testGetImages() {
        Flowable.fromPublisher(movie.images(THOR_RAGNAROK_ID)).blockingFirst()
    }

    @Test
    fun testGetKeywords() {
        val keywords = Flowable.fromPublisher(movie.keywords(THOR_RAGNAROK_ID)).blockingFirst()

        assertNotNull(keywords.keywords.find { it.id == KEYWORD_NORSE_GOD_ID })
    }

    @Test
    fun testGetReleaseDates() {
        val releaseDates = Flowable.fromPublisher(movie.releaseDates(THOR_RAGNAROK_ID)).blockingFirst()

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
        val videos = Flowable.fromPublisher(movie.videos(THOR_RAGNAROK_ID)).blockingFirst()

        assertTrue(videos.results.isNotEmpty())
    }

    @Test
    fun testGetTranslations() {
        val translations = Flowable.fromPublisher(movie.translations(THOR_RAGNAROK_ID)).blockingFirst()

        assertEquals(
            LATVIAN_TRANSLATION_NAME,
            translations.translations.find { it.country == CountryCode.LV }?.name
        )
    }

    @Test
    fun testGetRecommendations() {
        val recommendations = Flowable.fromPublisher(movie.recommendations(THOR_RAGNAROK_ID)).blockingFirst()

        assertTrue(recommendations.results.isNotEmpty())
    }

    @Test
    fun testGetSimilar() {
        val similar = Flowable.fromPublisher(movie.similar(THOR_RAGNAROK_ID)).blockingFirst()

        assertTrue(similar.results.isNotEmpty())
    }

    @Test
    fun testGetReviews() {
        val reviews = Flowable.fromPublisher(movie.reviews(THOR_RAGNAROK_ID)).blockingFirst()

        assertTrue(reviews.results.isNotEmpty())
    }

    @Test
    fun testGetLists() {
        val lists = Flowable.fromPublisher(movie.lists(THOR_RAGNAROK_ID)).blockingFirst()

        assertTrue(lists.results.isNotEmpty())
    }

    @Test
    fun testGetLatest() {
        Flowable.fromPublisher(movie.latest()).blockingFirst()
    }

    @Test
    fun testGetNowPlaying() {
        Flowable.fromPublisher(movie.nowPlaying()).blockingFirst()
    }

    @Test
    fun testGetPopular() {
        Flowable.fromPublisher(movie.popular()).blockingFirst()
    }

    @Test
    fun testGetTopRated() {
        Flowable.fromPublisher(movie.topRated()).blockingFirst()
    }

    @Test
    fun testGetUpcoming() {
        Flowable.fromPublisher(movie.upcoming()).blockingFirst()
    }
}
