package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.OriginalLanguageDeserializer

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvShowDetailsMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvShowChangesChangeItemMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
}

internal interface TvShowContentRatingsRatingMixin {
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvShowEpisodeGroupsTvShowEpisodeGroupMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvShowExternalIdsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvShowScreenTheatricallyScreenedResultMixin
