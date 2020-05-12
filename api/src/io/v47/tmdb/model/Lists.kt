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

import com.neovisionaries.i18n.LanguageCode

data class ListDetails(
    val createdBy: String?,
    val description: String?,
    val favoriteCount: Int?,
    override val id: String?,
    val items: List<MovieListResult> = emptyList(),
    val itemCount: Int?,
    val language: LanguageCode?,
    val listType: String?,
    val name: String?,
    val posterPath: String?
) : TmdbType(), TmdbStringId

data class ItemStatus(
    override val id: String?,
    val itemPresent: Boolean?
) : TmdbType(), TmdbStringId

/*
data class ListDetailsV4(
    @JsonProperty("iso_639_1")
    val language: LanguageCode?,
    val id: Int?,
    override val page: Int?,
    @JsonProperty("iso_3166_1")
    val country: CountryCode?,
    override val totalResults: Int?,
    val objectIds: Map<String, String> = emptyMap(),
    val revenue: String?,
    override val totalPages: Int?,
    val name: String?,
    val public: Boolean?,
    val comments: Map<String, Any> = emptyMap(),
    val sortBy: String?,
    val description: String?,
    val backdropPath: String?,
    @JsonDeserialize(contentUsing = MovieTvPersonListResultDeserializer::class)
    override val results: List<MovieTvPersonListResult> = emptyList(),
    val averageRating: Double?,
    val runtime: Int?,
    val createdBy: CreatedBy?,
    val posterPath: String?
) : TmdbType(), Paginated<MovieTvPersonListResult> {

    data class CreatedBy(
        val gravatarHash: String?,
        val name: String?,
        val username: String?,
        val id: String?
    ) : TmdbType()
}

data class ItemStatusV4(
    val mediaType: String?,
    val success: Boolean?,
    val statusMessage: String?,
    val id: Int?,
    val mediaId: Int?,
    val statusCode: Int?
) : TmdbType()
*/
