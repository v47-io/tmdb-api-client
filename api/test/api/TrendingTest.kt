package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Test

class TrendingTest : AbstractTmdbTest() {
    @Test
    fun testGetTrending() {
        client.trending.get().blockingFirst()
    }
}
