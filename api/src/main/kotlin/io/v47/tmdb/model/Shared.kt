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
@file:Suppress("ObjectPropertyName", "ObjectPropertyNaming", "EnumEntryName", "EnumNaming")

package io.v47.tmdb.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import java.time.Instant

data class Title(
    val title: String?,
    @param:JsonProperty("iso_3166_1")
    val country: CountryCode?,
    val type: String?
) : TmdbType()

enum class MediaType {
    Movie, Tv, TvEpisode, TvSeason, Person
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ImageListResult(
    val aspectRatio: Double?,
    val filePath: String?,
    val fileType: String?,
    val height: Int?,
    override val id: String?,
    @param:JsonProperty("iso_639_1")
    val language: LanguageCode?,
    @param:JsonProperty("iso_3166_1")
    val country: CountryCode?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
) : TmdbType(), TmdbStringId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class VideoListResult(
    override val id: String?,
    @param:JsonProperty("iso_639_1")
    val language: LanguageCode?,
    @param:JsonProperty("iso_3166_1")
    val country: CountryCode?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: VideoType?,
    val official: Boolean?,
    val publishedAt: Instant?,
) : TmdbType(), TmdbStringId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TranslationListResult(
    @param:JsonProperty("iso_639_1")
    val language: LanguageCode?,
    @param:JsonProperty("iso_3166_1")
    val country: CountryCode?,
    val name: String?,
    val englishName: String?,
    val data: TranslationData?
) : TmdbType() {
    data class TranslationData(
        val name: String?,
        val title: String?,
        val overview: String?,
        val tagline: String?,
        val runtime: Int?,
        val homepage: String?
    ) : TmdbType()
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PaginatedListResults<out T : Any>(
    override val id: Int?,
    override val page: Int,
    override val results: List<T> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<T>

enum class Release {
    ZERO_INDEX_PADDING,
    Premiere,
    `Theatrical Limited`,
    Theatrical,
    Digital,
    Physical,
    Tv
}

object VideoSizes {
    const val `360P` = 360
    const val `480P` = 480
    const val `720P` = 720
    const val `1080P` = 1080
}

enum class VideoType {
    Trailer,
    Teaser,
    Clip,
    Featurette,
    `Opening Credits`,
    `Behind the Scenes`,
    Bloopers,
    Recap
}

interface AppendRequest {
    val value: String
}
