package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class TvEpisodesTest : AbstractTmdbTest() {
    companion object {
        const val AGENTS_OF_SHIELD_ID = 1403
        const val SEASON = 5
        const val EPISODE = 7

        const val S05E07_ID = 1408720
    }

    private val tvEpisode by lazy { client.tvEpisode }

    @Test
    fun testGetDetailsAndImages() {
        val results = tvEpisode.details(
            AGENTS_OF_SHIELD_ID,
            SEASON,
            EPISODE,
            append = *TvEpisodeRequest.values()
        ).blockingFirst()

        assertNotNull(results.images)
    }

    @Test
    fun testGetChanges() {
        tvEpisode.changes(S05E07_ID).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        tvEpisode.credits(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetExternalIds() {
        tvEpisode.externalIds(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetImages() {
        tvEpisode.images(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetTranslations() {
        tvEpisode.translations(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }

    @Test
    fun testGetVideos() {
        tvEpisode.videos(AGENTS_OF_SHIELD_ID, SEASON, EPISODE).blockingFirst()
    }
}
