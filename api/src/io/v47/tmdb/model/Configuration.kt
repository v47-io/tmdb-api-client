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
    val nativeName: String?,
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
