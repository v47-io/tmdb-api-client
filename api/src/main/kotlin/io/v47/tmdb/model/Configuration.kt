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
package io.v47.tmdb.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.ImageSizeDeserializer
import io.v47.tmdb.jackson.serialization.ImageSizeSerializer

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Configuration(
    val images: Images = Images(null, null),
    val changeKeys: List<String> = emptyList()
) : TmdbType() {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Images(
        val baseUrl: String?,
        val secureBaseUrl: String?,
        @param:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @param:JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val backdropSizes: List<ImageSize> = emptyList(),
        @param:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @param:JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val logoSizes: List<ImageSize> = emptyList(),
        @param:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @param:JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val posterSizes: List<ImageSize> = emptyList(),
        @param:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @param:JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val profileSizes: List<ImageSize> = emptyList(),
        @param:JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @param:JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val stillSizes: List<ImageSize> = emptyList()
    ) : TmdbType()
}

sealed interface ImageSize {
    val value: Int

    data class Width(override val value: Int) : ImageSize, Comparable<Width> {
        init {
            require(value >= 0) { "Width must be >= 0" }
        }

        override fun compareTo(other: Width) = value.compareTo(other.value)

        override fun toString() = "w$value"
    }

    data class Height(override val value: Int) : ImageSize, Comparable<Height> {
        init {
            require(value >= 0) { "Height must be >= 0" }
        }

        override fun compareTo(other: Height) = value.compareTo(other.value)

        override fun toString() = "h$value"
    }

    data object Original : ImageSize {
        override val value = Int.MAX_VALUE

        override fun toString() = "original"

        @Suppress("unused")
        private fun readResolve(): Any = Original
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Country(
    @param:JsonProperty("iso_3166_1")
    val code: CountryCode?,
    val englishName: String?,
    val nativeName: String?,
    val name: String?
) : TmdbType()

data class Jobs(
    val department: String?,
    val jobs: List<String> = emptyList()
) : TmdbType()

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Language(
    @param:JsonProperty("iso_639_1")
    val code: LanguageCode?,
    val englishName: String?,
    val name: String
) : TmdbType()

data class Timezones(
    @param:JsonProperty("iso_3166_1")
    val country: CountryCode?,
    val zones: List<String> = emptyList()
) : TmdbType()
