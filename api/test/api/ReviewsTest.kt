package io.v47.tmdb.api

import io.v47.tmdb.model.MediaType.Movie
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReviewsTest : AbstractTmdbTest() {
    companion object {
        const val THOR_RAGNAROK_REVIEW_ID = "59f09b979251416ac10169f7"
        const val REVIEW_AUTHOR_NAME = "Gimly"
        const val REVIEW_MOVIE_TITLE = "Thor: Ragnarok"
    }

    @Test
    fun testGetDetails() {
        val review = client.review.details(THOR_RAGNAROK_REVIEW_ID).blockingFirst()

        assertEquals(REVIEW_AUTHOR_NAME, review.author)
        assertEquals(REVIEW_MOVIE_TITLE, review.mediaTitle)
        assertEquals(Movie, review.mediaType)
    }
}
