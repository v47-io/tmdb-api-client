/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.CreditMediaDeserializer
import io.v47.tmdb.jackson.deserialization.CreditPersonKnownForDeserializer
import io.v47.tmdb.jackson.deserialization.OriginalLanguageDeserializer
import io.v47.tmdb.model.CreditMedia
import io.v47.tmdb.model.CreditPersonKnownFor

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditsMixin {
    @get:JsonDeserialize(using = CreditMediaDeserializer::class)
    val media: CreditMedia?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditMediaMovieMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditMediaTvMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditMediaTvEpisodeMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditMediaTvSeasonMixin

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditPersonMixin {
    @get:JsonDeserialize(contentUsing = CreditPersonKnownForDeserializer::class)
    val knownFor: List<CreditPersonKnownFor>
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditPersonKnownForMovieMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal interface CreditPersonKnownForTvMixin {
    @get:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?
}
