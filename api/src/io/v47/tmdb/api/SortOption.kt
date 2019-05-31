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
