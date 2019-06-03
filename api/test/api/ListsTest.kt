package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ListsTest : AbstractTmdbTest() {
    companion object {
        const val THOR_RAGNAROK_ID = 284053
    }

    @Test
    fun testGetDetails() {
        val result = client.list.details("1").blockingFirst()
        assertTrue(result.items.isNotEmpty())
    }

    @Test
    fun testCheckItemStatus() {
        val result = client.list.checkItemStatus("1", THOR_RAGNAROK_ID).blockingFirst()
        assertTrue(result.itemPresent ?: false)
    }
}
