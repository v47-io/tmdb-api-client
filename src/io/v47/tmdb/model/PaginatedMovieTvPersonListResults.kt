package io.v47.tmdb.model

internal data class PaginatedMovieTvPersonListResults(
    override val page: Int?,
    override val results: List<MovieTvPersonListResult> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<MovieTvPersonListResult>
