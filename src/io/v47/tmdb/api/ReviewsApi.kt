package io.v47.tmdb.api

import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.ReviewDetails

class ReviewsApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the full details of a review by ID
     *
     * @param reviewId The id of the review
     */
    fun details(reviewId: String) =
        httpExecutor.execute(get<ReviewDetails>("/review/$reviewId"))
}
