package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.ImageSizeDeserializer
import io.v47.tmdb.jackson.serialization.ImageSizeSerializer
import io.v47.tmdb.model.ImageSize

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface ConfigurationMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface ConfigurationImagesMixin {
    @get:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
    @get:JsonSerialize(contentUsing = ImageSizeSerializer::class)
    val backdropSizes: List<ImageSize>
    @get:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
    @get:JsonSerialize(contentUsing = ImageSizeSerializer::class)
    val logoSizes: List<ImageSize>
    @get:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
    @get:JsonSerialize(contentUsing = ImageSizeSerializer::class)
    val posterSizes: List<ImageSize>
    @get:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
    @get:JsonSerialize(contentUsing = ImageSizeSerializer::class)
    val profileSizes: List<ImageSize>
    @get:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
    @get:JsonSerialize(contentUsing = ImageSizeSerializer::class)
    val stillSizes: List<ImageSize>
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CountryMixin {
    @get:JsonProperty("iso_3166_1")
    val code: CountryCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface LanguageMixin {
    @get:JsonProperty("iso_639_1")
    val code: LanguageCode?
}

internal interface TimezonesMixin {
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}
