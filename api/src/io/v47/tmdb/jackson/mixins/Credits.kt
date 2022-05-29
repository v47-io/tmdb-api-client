/**
 * Copyright 2022 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
