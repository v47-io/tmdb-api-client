package io.v47.tmdb.api

import io.reactivex.Flowable
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ImagesTest : AbstractTmdbTest() {
    companion object {
        const val NETFLIX_LOGO_ID = "wwemzKWzjKYJFfCeiB57q3r4Bcm"
    }

    @Test
    fun testDownloadImage() {
        val result = Flowable.fromPublisher(client.images.download("$NETFLIX_LOGO_ID.svg")).blockingFirst()
        assertTrue(result.isNotEmpty())
    }
}
