package io.v47.tmdb.api

import io.v47.tmdb.model.TvEpisodeGroupType
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TvEpisodeGroupsTest : AbstractTmdbTest() {
    companion object {
        const val SUPERGIRL_EPISODE_GROUPS_ID = "5ad02f49925141390400100e"
    }

    @Test
    fun testGetEpisodeGroups() {
        val episodeGroups = client.tvEpisodeGroup.details(SUPERGIRL_EPISODE_GROUPS_ID).blockingFirst()

        assertEquals(TvEpisodeGroupType.Production, episodeGroups.type)
        assertTrue(episodeGroups.groups.isNotEmpty())
    }
}
