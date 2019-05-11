package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.LanguageCode

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvSeasonDetailsMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvSeasonChangesChangeItemMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvSeasonChangesChangeValueMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface TvSeasonExternalIdsMixin
