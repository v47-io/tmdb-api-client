/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.api

import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.MovieTvPersonListResult
import io.v47.tmdb.model.Paginated
import io.v47.tmdb.model.PaginatedMovieTvPersonListResults
import org.reactivestreams.Publisher

class TrendingApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get the daily or weekly trending items. The daily trending list tracks items over the
     * period of a day while items have a 24 hour half life. The weekly list tracks items over
     * a 7 day period, with a 7 day half life.
     *
     * @param mediaType The media type to retrieve trending items for
     * @param timeWindow The time window to select trending items from
     */
    fun get(
        mediaType: TrendingMediaType = TrendingMediaType.All,
        timeWindow: TrendingTimeWindow = TrendingTimeWindow.Day
    ): Publisher<out Paginated<MovieTvPersonListResult>> =
        http.get<PaginatedMovieTvPersonListResults>(
            "/trending/{mediaType}/{timeWindow}"
        ) {
            pathVar("mediaType", mediaType.name.lowercase())
            pathVar("timeWindow", timeWindow.name.lowercase())
        }
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
