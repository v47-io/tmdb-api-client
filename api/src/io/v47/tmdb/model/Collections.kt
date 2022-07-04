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
import java.time.LocalDate

data class CollectionDetails(
    override val id: Int?,
    val name: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val parts: List<Part> = emptyList()
) : TmdbType(), TmdbIntId {
    data class Part(
        val adult: Boolean?,
        val backdropPath: String?,
        val genreIds: List<Int> = emptyList(),
        override val id: Int?,
        val originalLanguage: LanguageCode?,
        val originalTitle: String?,
        val overview: String?,
        val releaseDate: LocalDate?,
        val posterPath: String?,
        val title: String?,
        val video: Boolean?,
        val voteAverage: Double?,
        val voteCount: Int?,
        val popularity: Double?
    ) : TmdbType(), TmdbIntId
}

data class CollectionImages(
    override val id: Int?,
    val backdrops: List<ImageListResult> = emptyList(),
    val posters: List<ImageListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class CollectionTranslations(
    override val id: Int?,
    val translations: List<CollectionTranslation> = emptyList()
) : TmdbType(), TmdbIntId

data class CollectionTranslation(
    val country: CountryCode?,
    val language: LanguageCode?,
    val name: String?,
    val englishName: String?,
    val data: CollectionTranslationData? = null
) : TmdbType()

data class CollectionTranslationData(
    val title: String?,
    val overview: String?,
    val homepage: String?
) : TmdbType()
