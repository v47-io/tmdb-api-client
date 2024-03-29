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

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import java.io.Serializable
import java.time.Instant
import java.time.LocalDate

interface MovieTvPersonListResult : TmdbIntId, Serializable {
    override val id: Int?
    val mediaType: MediaType?
}

data class MovieListResult(
    val posterPath: String?,
    val adult: Boolean?,
    val overview: String?,
    val releaseDate: LocalDate?,
    val genreIds: List<Int> = emptyList(),
    override val id: Int?,
    val originalTitle: String?,
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

data class TvListResult(
    val posterPath: String?,
    val popularity: Double?,
    override val id: Int?,
    val backdropPath: String?,
    val voteAverage: Double?,
    val overview: String?,
    val firstAirDate: LocalDate?,
    val networks: List<TvListNetwork> = emptyList(),
    val originCountry: List<String> = emptyList(),
    val genreIds: List<Int> = emptyList(),
    val originalLanguage: LanguageCode?,
    val voteCount: Int?,
    val rating: Double?,
    val name: String?,
    val originalName: String?,
    override val mediaType: MediaType?,
    val adult: Boolean?,
) : TmdbType(), MovieTvPersonListResult {
    data class TvListNetwork(
        val id: Long?,
        val logo: TvListNetworkLogo?,
        val name: String?,
        val originCountry: String?
    ) : TmdbType() {
        data class TvListNetworkLogo(
            val path: String?,
            val aspectRatio: Double?
        ) : TmdbType()
    }
}

data class TvEpisodeListResult(
    val airDate: LocalDate?,
    val episodeNumber: Int?,
    val name: String?,
    val overview: String?,
    override val id: Int?,
    val productionCode: String?,
    val runtime: Int?,
    val seasonNumber: String?,
    val showId: Int?,
    val stillPath: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val rating: Double?,
) : TmdbType(), TmdbIntId

data class Title(
    val title: String?,
    val country: CountryCode?,
    val type: String?
) : TmdbType()

enum class MediaType {
    Movie, Tv, TvEpisode, TvSeason, Person
}

interface IPerson : TmdbIntId {
    val adult: Boolean?
    val gender: Gender?
    override val id: Int?
    val name: String?
    val profilePath: String?
    val knownForDepartment: String?
    val popularity: Double?
    val originalName: String?
}

enum class Gender {
    Unset,
    Female,
    Male,
    Other
}

interface ICastMember : IPerson {
    val character: String?
}

interface ICrewMember : IPerson {
    val department: String?
    val job: String?
}

data class PersonListResult(
    override val profilePath: String?,
    override val adult: Boolean?,
    override val gender: Gender?,
    override val id: Int?,
    override val mediaType: MediaType?,
    val knownFor: List<MovieTvPersonListResult> = emptyList(),
    override val knownForDepartment: String?,
    val creditId: String?,
    override val name: String?,
    override val popularity: Double?,
    override val originalName: String?
) : TmdbType(), IPerson, MovieTvPersonListResult

data class Genre(
    override val id: Int?,
    val name: String?
) : TmdbType(), TmdbIntId

data class CollectionInfo(
    override val id: Int?,
    val name: String?,
    val originalName: String?,
    val overview: String?,
    val originalLanguage: LanguageCode?,
    val posterPath: String?,
    val backdropPath: String?,
    val adult: Boolean?
) : TmdbType(), TmdbIntId

data class CompanyInfo(
    override val id: Int?,
    val logoPath: String?,
    val name: String?,
    val originCountry: CountryCode?
) : TmdbType(), TmdbIntId

data class CreditListResult(
    override val adult: Boolean?,
    val castId: Int?,
    override val character: String?,
    override val knownForDepartment: String?,
    override val popularity: Double?,
    val creditId: String?,
    override val department: String?,
    override val gender: Gender?,
    override val id: Int?,
    override val job: String?,
    override val name: String?,
    override val originalName: String?,
    val order: Int?,
    override val profilePath: String?
) : TmdbType(), ICastMember, ICrewMember

data class ImageListResult(
    val aspectRatio: Double?,
    val filePath: String?,
    val fileType: String?,
    val height: Int?,
    override val id: String?,
    val language: LanguageCode?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
) : TmdbType(), TmdbStringId

data class VideoListResult(
    override val id: String?,
    val language: LanguageCode?,
    val country: CountryCode?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: VideoType?,
    val official: Boolean?,
    val publishedAt: Instant?,
) : TmdbType(), TmdbStringId

data class TranslationListResult(
    val language: LanguageCode?,
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

data class PaginatedListResults<out T : Any>(
    override val id: Int?,
    override val page: Int,
    override val results: List<T> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<T>

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

data class CastMember(
    override val id: Int?,
    val originalLanguage: LanguageCode?,
    val episodeCount: Int?,
    val overview: String?,
    val originCountry: List<CountryCode> = emptyList(),
    val originalName: String?,
    val genreIds: List<Int> = emptyList(),
    val name: String?,
    val mediaType: MediaType?,
    val posterPath: String?,
    val firstAirDate: LocalDate?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val character: String?,
    val backdropPath: String?,
    val popularity: Double?,
    val creditId: String?,
    val originalTitle: String?,
    val video: Boolean?,
    val releaseDate: LocalDate?,
    val title: String?,
    val adult: Boolean?,
    val order: Int?,
) : TmdbType(), TmdbIntId

data class CrewMember(
    override val id: Int?,
    val department: String?,
    val originalLanguage: LanguageCode?,
    val episodeCount: Int?,
    val job: String?,
    val overview: String?,
    val originCountry: List<CountryCode> = emptyList(),
    val originalName: String?,
    val voteCount: Int?,
    val name: String?,
    val mediaType: MediaType?,
    val popularity: Double?,
    val creditId: String?,
    val backdropPath: String?,
    val firstAirDate: LocalDate?,
    val voteAverage: Double?,
    val genreIds: List<Int> = emptyList(),
    val posterPath: String?,
    val originalTitle: String?,
    val video: Boolean?,
    val title: String?,
    val adult: Boolean?,
    val releaseDate: LocalDate?
) : TmdbType(), TmdbIntId

enum class CreditType {
    Cast,
    Crew
}

enum class MovieStatus {
    Rumored,
    Planned,
    `In Production`,
    `Post Production`,
    Released,
    Canceled
}

enum class TvShowStatus {
    `Returning Series`,
    Planned,
    `In Production`,
    Ended,
    Canceled,
    Pilot
}

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

enum class TvShowType {
    Scripted,
    Reality,
    Documentary,
    News,
    `Talk Show`,
    Miniseries,
    Video
}

data class ReviewAuthorDetails(
    val name: String?,
    val username: String?,
    val avatarPath: String?,
    val rating: Double?
) : TmdbType()
