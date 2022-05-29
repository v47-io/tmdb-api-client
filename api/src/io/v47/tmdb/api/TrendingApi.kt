/**
 * Copyright 2022 The tmdb-api-v2 Authors
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

import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.MovieTvPersonListResult
import io.v47.tmdb.model.Paginated
import io.v47.tmdb.model.PaginatedMovieTvPersonListResults
import org.reactivestreams.Publisher

@Suppress("UNCHECKED_CAST")
class TrendingApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the daily or weekly trending items. The daily trending list tracks items over the
     * period of a day while items have a 24 hour half life. The weekly list tracks items over
     * a 7 day period, with a 7 day half life.
     *
     * @param mediaType The media type to retrieve trending items for
     * @param timeWindow The time window to select trending items from
     */
    @Suppress("UNCHECKED_CAST")
    fun get(
        mediaType: TrendingMediaType = TrendingMediaType.All,
        timeWindow: TrendingTimeWindow = TrendingTimeWindow.Day
    ): Publisher<out Paginated<MovieTvPersonListResult>> =
        httpExecutor.execute(
            io.v47.tmdb.http.get<PaginatedMovieTvPersonListResults>(
                "/trending/{mediaType}/{timeWindow}"
            ) {
                pathVar("mediaType", mediaType.name.toLowerCase())
                pathVar("timeWindow", timeWindow.name.toLowerCase())
            }
        )
}

enum class TrendingMediaType {
    All,
    Movie,
    Tv,
    Person
}

enum class TrendingTimeWindow {
    Day,
    Week
}
