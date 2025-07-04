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
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.jackson.deserialization.OriginalLanguageDeserializer
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MovieDetails(
    val adult: Boolean?,
    val backdropPath: String?,
    val belongsToCollection: CollectionInfo?,
    val budget: Int?,
    val genres: List<Genre> = emptyList(),
    val homepage: String?,
    override val id: Int?,
    val imdbId: String?,
    val originCountry: List<CountryCode>?,
    @param:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val productionCompanies: List<CompanyInfo> = emptyList(),
    val productionCountries: List<Country> = emptyList(),
    val releaseDate: LocalDate?,
    val revenue: Long?,
    val runtime: Int?,
    val spokenLanguages: List<Language> = emptyList(),
    val status: MovieStatus?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?,

    // append_to_response
    val alternativeTitles: MovieAlternativeTitles?,
    val changes: MovieChanges?,
    val credits: MovieCredits?,
    val externalIds: MovieExternalIds?,
    val images: MovieImages?,
    val keywords: MovieKeywords?,
    val releaseDates: MovieReleaseDates?,
    val videos: MovieVideos?,
    val translations: MovieTranslations?,
    val recommendations: PaginatedListResults<MovieListResult>?,
    val similar: PaginatedListResults<MovieListResult>?,
    val reviews: MovieReviews?,
    val lists: MovieLists?
) : TmdbType(), TmdbIntId

data class MovieAlternativeTitles(
    override val id: Int?,
    val titles: List<Title> = emptyList()
) : TmdbType(), TmdbIntId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MovieChanges(
    override val page: Int?,
    @param:JsonProperty("changes")
    override val results: List<Change> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<MovieChanges.Change> {
    data class Change(
        val key: String?,
        val items: List<ChangeItem> = emptyList()
    ) : TmdbType()

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ChangeItem(
        override val id: String?,
        val action: String?,
        val time: String?,
        @param:JsonProperty("iso_639_1")
        val language: LanguageCode?,
        val value: Any?,
        val originalValue: Any?,
        @param:JsonProperty("iso_3166_1")
        val country: CountryCode?
    ) : TmdbType(), TmdbStringId
}

data class MovieCredits(
    override val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList()
) : TmdbType(), TmdbIntId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MovieExternalIds(
    override val id: Int?,
    val imdbId: String?,
    val facebookId: String?,
    val instagramId: String?,
    val twitterId: String?,
    val wikidataId: String?
) : TmdbType(), TmdbIntId

data class MovieImages(
    override val id: Int?,
    val backdrops: List<ImageListResult> = emptyList(),
    val posters: List<ImageListResult> = emptyList(),
    val logos: List<ImageListResult> = emptyList(),
) : TmdbType(), TmdbIntId

data class MovieKeywords(
    override val id: Int?,
    val keywords: List<Keyword> = emptyList()
) : TmdbType(), TmdbIntId

data class MovieReleaseDates(
    override val id: Int?,
    val results: List<ReleaseDates> = emptyList()
) : TmdbType(), TmdbIntId {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ReleaseDates(
        @param:JsonProperty("iso_3166_1")
        val country: CountryCode?,
        val releaseDates: List<MovieReleaseInfo> = emptyList()
    ) : TmdbType() {
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
        data class MovieReleaseInfo(
            val certification: String?,
            @param:JsonProperty("iso_639_1")
            val language: LanguageCode?,
            val note: String?,
            val releaseDate: LocalDate?,
            val type: Release?,
            val descriptors: List<String>?
        ) : TmdbType()
    }
}

data class MovieVideos(
    override val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class MovieTranslations(
    override val id: Int?,
    val translations: List<TranslationListResult> = emptyList()
) : TmdbType(), TmdbIntId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MovieReviews(
    override val id: Int?,
    override val page: Int?,
    override val results: List<ReviewDetails> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<ReviewDetails>

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MovieLists(
    override val id: Int?,
    override val page: Int?,
    override val results: List<ListDetails> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<ListDetails>

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MovieListResult(
    val posterPath: String?,
    val adult: Boolean?,
    val overview: String?,
    val releaseDate: LocalDate?,
    val genreIds: List<Int> = emptyList(),
    override val id: Int?,
    val originalTitle: String?,
    @param:JsonDeserialize(using = OriginalLanguageDeserializer::class)
    val originalLanguage: LanguageCode?,
    val title: String?,
    val backdropPath: String?,
    val popularity: Double?,
    val voteCount: Int?,
    val video: Boolean?,
    val voteAverage: Double?,
    val rating: Double?,
    override val mediaType: MediaType?
) : TmdbType(), TmdbIntId, MovieTvPersonListResult

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PaginatedMovieListResultsWithDates(
    override val id: Int?,
    override val page: Int?,
    override val results: List<MovieListResult> = emptyList(),
    val dates: Dates?,
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<MovieListResult> {
    data class Dates(
        val maximum: LocalDate?,
        val minimum: LocalDate?
    ) : TmdbType()
}

@Suppress("EnumEntryName", "EnumNaming")
enum class MovieStatus {
    Rumored,
    Planned,
    `In Production`,
    `Post Production`,
    Released,
    Canceled
}
