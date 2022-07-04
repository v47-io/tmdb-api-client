/**
 * Copyright 2022 The tmdb-api-client Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.v47.tmdb.api

import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.ReviewDetails

class ReviewsApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the full details of a review by ID
     *
     * @param reviewId The id of the review
     */
    fun details(reviewId: String) =
        httpExecutor.execute(
            get<ReviewDetails>("/review/{reviewId}") {
                pathVar("reviewId", reviewId)
            }
        )
}
