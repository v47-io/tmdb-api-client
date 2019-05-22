package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.v47.tmdb.jackson.deserialization.MovieTvPersonListResultDeserializer
import io.v47.tmdb.model.MovieTvPersonListResult

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
interface PaginatedMovieTvPersonListResultsMixin {
    @get:JsonDeserialize(contentUsing = MovieTvPersonListResultDeserializer::class)
    val results: List<MovieTvPersonListResult>
}
