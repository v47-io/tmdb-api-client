/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.MovieTvPersonListResultDeserializer
import io.v47.tmdb.model.MovieTvPersonListResult

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PersonDetailsMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PersonExternalIdsMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PersonTaggedImagesMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PersonTaggedImagesTaggedImageMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?

    @get:JsonDeserialize(using = MovieTvPersonListResultDeserializer::class)
    val media: MovieTvPersonListResult?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PersonTranslationsPersonTranslationMixin {
    @get:JsonProperty("iso_639_1")
    val language: LanguageCode?

    @get:JsonProperty("iso_3166_1")
    val country: CountryCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PersonChangesPersonChangePersonChangeItemMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PeoplePopularMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface PeoplePopularPopularPersonMixin {
    @get:JsonDeserialize(contentUsing = MovieTvPersonListResultDeserializer::class)
    val knownFor: List<MovieTvPersonListResult>
}
