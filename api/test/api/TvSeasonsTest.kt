package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TvSeasonsTest : AbstractTmdbTest() {
    companion object {
        const val AGENTS_OF_SHIELD_ID = 1403
        const val SEASON = 5

        const val SEASON_ID = 93742
    }

    private val tvSeason by lazy { client.tvSeason }

    @Test
    fun testGetDetailsAndCredits() {
        val details = tvSeason.details(
            AGENTS_OF_SHIELD_ID,
            SEASON,
            append = *TvSeasonRequest.values()
        ).blockingFirst()

        Assertions.assertNotNull(details.credits)
    }

    @Test
    fun testGetChanges() {
        tvSeason.changes(SEASON_ID).blockingFirst()
    }

    @Test
    fun testGetCredits() {
        tvSeason.credits(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }

    @Test
    fun testGetExternalIds() {
        tvSeason.externalIds(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }

    @Test
    fun testGetImages() {
        tvSeason.images(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }

    @Test
    fun testGetVideos() {
        tvSeason.videos(AGENTS_OF_SHIELD_ID, SEASON).blockingFirst()
    }
}
