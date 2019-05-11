package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.model.MovieChanges

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieDetailsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieChangesMixin {
    @get:JsonProperty("changes")
    val results: List<MovieChanges.Change>
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieChangesChangeItemMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieExternalIdsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieReleaseDatesReleaseDatesMixin {
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieReleaseDatesReleaseDatesMovieReleaseInfoMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieReviewsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface MovieListsMixin
