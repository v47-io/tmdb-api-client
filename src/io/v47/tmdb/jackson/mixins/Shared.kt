package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.MovieTvPersonListResultDeserializer
import io.v47.tmdb.model.MovieTvPersonListResult

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieListResultMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvListResultMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvListResultTvListNetworkMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvListResultTvListNetworkTvListNetworkLogoMixin

internal interface TitleMixin {
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PersonListResultMixin {
    @get:JsonDeserialize(contentUsing = MovieTvPersonListResultDeserializer::class)
    val knownFor: List<MovieTvPersonListResult>
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CollectionInfoMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CompanyInfoMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditListResultMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface ImageListResultMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
}

internal interface VideoListResultMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TranslationListResultMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PaginatedListResultsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PaginatedMovieListResultsWithDatesMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CastMemberMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CrewMemberMixin
