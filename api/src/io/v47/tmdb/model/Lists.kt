/**
 * The Clear BSD License
 *
 * Copyright (c) 2022 the tmdb-api-client authors
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
