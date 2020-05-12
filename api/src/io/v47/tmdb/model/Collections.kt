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
