package io.v47.tmdb.api

import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.MovieTvPersonListResult
import io.v47.tmdb.model.Paginated
import io.v47.tmdb.model.PaginatedMovieTvPersonListResults

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
    ): Paginated<MovieTvPersonListResult> = httpExecutor.execute(
        get<PaginatedMovieTvPersonListResults>(
            "/trending/${mediaType.name.toLowerCase()}/${timeWindow.name.toLowerCase()}"
        )
    ) as Paginated<MovieTvPersonListResult>
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
