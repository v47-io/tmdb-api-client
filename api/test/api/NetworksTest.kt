package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NetworksTest : AbstractTmdbTest() {
    companion object {
        const val TNT_ID = 41
        const val TNT_NAME = "TNT"
        const val TNT_ALTERNATIVE_NAME = "Turner Network Television"
    }

    @Test
    fun testGetDetails() {
        val details = client.network.details(TNT_ID).blockingFirst()
        assertEquals(TNT_NAME, details.name)
    }

    @Test
    fun testGetAlternativeNames() {
        val alternativeNames = client.network.alternativeNames(TNT_ID).blockingFirst()
        assertEquals(TNT_ALTERNATIVE_NAME, alternativeNames.results.firstOrNull()?.name)
    }

    @Test
    fun testGetImages() {
        val images = client.network.images(TNT_ID).blockingFirst()
        assertTrue(images.logos.isNotEmpty())
    }
}
