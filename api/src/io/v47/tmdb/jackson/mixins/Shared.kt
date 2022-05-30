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

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.MovieTvPersonListResultDeserializer
import io.v47.tmdb.jackson.deserialization.OriginalLanguageDeserializer
import io.v47.tmdb.model.MovieTvPersonListResult

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface MovieListResultMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface TvListResultMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface TvListResultTvListNetworkMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface TvListResultTvListNetworkTvListNetworkLogoMixin

internal interface TitleMixin {
    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PersonListResultMixin {
    @get:JsonDeserialize(contentUsing = MovieTvPersonListResultDeserializer::class)
    val knownFor: List<MovieTvPersonListResult>
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CollectionInfoMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CompanyInfoMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditListResultMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface ImageListResultMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface VideoListResultMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?

    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface TranslationListResultMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?

    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PaginatedListResultsMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PaginatedMovieListResultsWithDatesMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CastMemberMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CrewMemberMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface ReviewAuthorDetailsMixin
