/**
 * Copyright 2020 The tmdb-api-v2 Authors
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

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode

data class Configuration(
    val images: Images = Images(null, null),
    val changeKeys: List<String> = emptyList()
) : TmdbType() {
    data class Images(
        val baseUrl: String?,
        val secureBaseUrl: String?,
        val backdropSizes: List<ImageSize> = emptyList(),
        val logoSizes: List<ImageSize> = emptyList(),
        val posterSizes: List<ImageSize> = emptyList(),
        val profileSizes: List<ImageSize> = emptyList(),
        val stillSizes: List<ImageSize> = emptyList()
    ) : TmdbType()
}

sealed class ImageSize {
    abstract val value: Int
}

data class Width(override val value: Int) : ImageSize(), Comparable<Width> {
    override fun compareTo(other: Width) = value.compareTo(other.value)

    override fun toString() = "w$value"
}

data class Height(override val value: Int) : ImageSize(), Comparable<Height> {
    override fun compareTo(other: Height) = value.compareTo(other.value)

    override fun toString() = "h$value"
}

object Original : ImageSize() {
    override val value = Int.MAX_VALUE

    override fun toString() = "original"
}

data class Country(
    val code: CountryCode?,
    val englishName: String?,
    val name: String?
) : TmdbType()

data class Jobs(
    val department: String?,
    val jobs: List<String> = emptyList()
) : TmdbType()

data class Language(
    val code: LanguageCode?,
    val englishName: String?,
    val name: String
) : TmdbType()

data class Timezones(
    val country: CountryCode?,
    val zones: List<String> = emptyList()
) : TmdbType()
