/**
 * Copyright 2019 Alex Katlein <dev@vemilyus.com>
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
package io.v47.tmdb.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.utils.ImageSizeDeserializer
import io.v47.tmdb.utils.ImageSizeSerializer

data class Configuration(
    val images: Images = Images(null, null),
    val changeKeys: List<String> = emptyList()
) : TmdbType() {
    data class Images(
        val baseUrl: String?,
        val secureBaseUrl: String?,
        @JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val backdropSizes: List<ImageSize> = emptyList(),
        @JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val logoSizes: List<ImageSize> = emptyList(),
        @JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val posterSizes: List<ImageSize> = emptyList(),
        @JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val profileSizes: List<ImageSize> = emptyList(),
        @JsonDeserialize(contentUsing = ImageSizeDeserializer::class)
        @JsonSerialize(contentUsing = ImageSizeSerializer::class)
        val stillSizes: List<ImageSize> = emptyList()
    ) : TmdbType()
}

interface ImageSize {
    val value: Int
}

internal data class Width(override val value: Int) : ImageSize, Comparable<Width> {
    override fun compareTo(other: Width) = value.compareTo(other.value)

    override fun toString() = "w$value"
}

internal data class Height(override val value: Int) : ImageSize, Comparable<Height> {
    override fun compareTo(other: Height) = value.compareTo(other.value)

    override fun toString() = "h$value"
}

object Original : ImageSize {
    override val value = Int.MAX_VALUE

    override fun toString() = "original"
}

data class Country(
    @JsonProperty("iso_3166_1")
    val code: CountryCode?,
    val englishName: String?,
    val name: String?
) : TmdbType()

data class Jobs(
    val department: String?,
    val jobs: List<String> = emptyList()
) : TmdbType()

data class Language(
    @JsonProperty("iso_639_1")
    val code: LanguageCode?,
    val englishName: String?,
    val name: String
) : TmdbType()

data class Timezones(
    @JsonProperty("iso_3166_1")
    val country: CountryCode?,
    val zones: List<String> = emptyList()
) : TmdbType()
