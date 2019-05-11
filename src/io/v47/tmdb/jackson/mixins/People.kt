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
internal interface PersonDetailsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PersonExternalIdsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PersonTaggedImagesMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PersonTaggedImagesTaggedImageMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?

    @get:JsonDeserialize(using = MovieTvPersonListResultDeserializer::class)
    val media: MovieTvPersonListResult?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PersonTranslationsPersonTranslationMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PersonChangesPersonChangePersonChangeItemMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PeoplePopularMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface PeoplePopularPopularPersonMixin {
    @get:JsonDeserialize(contentUsing = MovieTvPersonListResultDeserializer::class)
    val knownFor: List<MovieTvPersonListResult>
}
