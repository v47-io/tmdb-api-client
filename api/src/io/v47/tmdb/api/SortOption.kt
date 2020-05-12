/**
 * Copyright 2020 The tmdb-api-v2 Authors
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

enum class SortOption(internal val value: String) {
    Popularity("popularity"),
    ReleaseDate("release_date"),
    Revenue("revenue"),
    PrimaryReleaseDate("primary_release_date"),
    OriginalTitle("original_title"),
    VoteAverage("vote_average"),
    VoteCount("vote_count"),
    FirstAirDate("first_air_date"),
    CreatedAt("created_at"),
    Title("title");

    companion object {
        val movieOptions = listOf(
            Popularity,
            ReleaseDate,
            Revenue,
            PrimaryReleaseDate,
            OriginalTitle,
            VoteAverage,
            VoteCount
        )

        val tvOptions = listOf(VoteAverage, FirstAirDate, Popularity)

        val genreOptions = listOf(CreatedAt)

        val listV4Options = listOf(ReleaseDate, Title, VoteAverage)
    }
}

enum class SortDirection(internal val value: String) {
    Ascending("asc"),
    Descending("desc")
}
