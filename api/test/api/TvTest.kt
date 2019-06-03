package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TvTest : AbstractTmdbTest() {
    companion object {
        const val AGENTS_OF_SHIELD_ID = 1403
        const val AGENTS_OF_SHIELD_NAME_ES = "Marvel's Agentes de S.H.I.E.L.D."

        const val GAME_OF_THRONES_ID = 1399
        const val SUPERGIRL_ID = 62688
    }

    private val tvShow by lazy { client.tvShow }

    @Test
    fun testGetDetailsAndTranslationsVideos() {
        val details = tvShow.details(
            AGENTS_OF_SHIELD_ID,
            language = LocaleCode.es_ES,
            append = *arrayOf(TvRequest.Translations, TvRequest.Videos)
        ).blockingFirst()

        assertEquals(AGENTS_OF_SHIELD_NAME_ES, details.name)
        assertNotNull(details.translations)
        assertNotNull(details.videos)
    }

    @Test
    fun testGetAlternativeTitles() {
        tvShow.alternativeTitles(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetChanges() {
        tvShow.changes(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetContentRatings() {
        tvShow.contentRatings(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        tvShow.credits(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetEpisodeGroups() {
        val episodeGroups = tvShow.episodeGroups(SUPERGIRL_ID).blockingFirst()
        assertTrue(episodeGroups.results.isNotEmpty())
    }

    @Test
    fun testGetExternalIds() {
        tvShow.externalIds(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetImages() {
        tvShow.images(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetKeywords() {
        tvShow.keywords(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetRecommendations() {
        tvShow.recommendations(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetReviews() {
        tvShow.reviews(AGENTS_OF_SHIELD_ID).blockingFirst()
    }

    @Test
    fun testGetScreenedTheatrically() {
        tvShow.screenedTheatrically(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetSimilar() {
        tvShow.similar(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetTranslations() {
        tvShow.translations(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetVideos() {
        tvShow.videos(GAME_OF_THRONES_ID).blockingFirst()
    }

    @Test
    fun testGetLatest() {
        tvShow.latest().blockingFirst()
    }

    @Test
    fun testGetAiringToday() {
        tvShow.getAiringToday().blockingFirst()
    }

    @Test
    fun testGetOnTheAir() {
        tvShow.getOnTheAir().blockingFirst()
    }

    @Test
    fun testGetPopular() {
        tvShow.getPopular().blockingFirst()
    }

    @Test
    fun testGetTopRated() {
        tvShow.getTopRated().blockingFirst()
    }
}
