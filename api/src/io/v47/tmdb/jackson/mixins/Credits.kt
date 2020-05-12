package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.CreditMediaDeserializer
import io.v47.tmdb.jackson.deserialization.CreditPersonKnownForDeserializer
import io.v47.tmdb.jackson.deserialization.OriginalLanguageDeserializer
import io.v47.tmdb.model.CreditMedia
import io.v47.tmdb.model.CreditPersonKnownFor

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditsMixin {
    @get:JsonDeserialize(using = CreditMediaDeserializer::class)
    val media: CreditMedia?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditMediaMovieMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditMediaTvMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditMediaTvEpisodeMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditMediaTvSeasonMixin

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditPersonMixin {
    @get:JsonDeserialize(contentUsing = CreditPersonKnownForDeserializer::class)
    val knownFor: List<CreditPersonKnownFor>
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditPersonKnownForMovieMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal interface CreditPersonKnownForTvMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}
