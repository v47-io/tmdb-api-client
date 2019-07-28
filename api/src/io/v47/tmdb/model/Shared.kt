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
@file:Suppress("ObjectPropertyName", "ObjectPropertyNaming", "EnumEntryName", "EnumNaming")

package io.v47.tmdb.model

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import java.time.LocalDate

interface MovieTvPersonListResult {
    val id: Int?
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
    override val mediaType: MediaType?
) : TmdbType(), MovieTvPersonListResult

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
    val name: String?,
    val originalName: String?,
    override val mediaType: MediaType?
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

data class Title(
    val title: String?,
    val country: CountryCode?,
    val type: String?
) : TmdbType()

enum class MediaType {
    Movie, Tv, Person
}

interface IPerson {
    val gender: Gender?
    val id: Int?
    val name: String?
    val profilePath: String?
}

enum class Gender {
    Unset,
    Female,
    Male
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
    val adult: Boolean?,
    override val gender: Gender?,
    override val id: Int?,
    override val mediaType: MediaType?,
    val knownFor: List<MovieTvPersonListResult> = emptyList(),
    val knownForDepartment: String?,
    val creditId: String?,
    override val name: String?,
    val popularity: Double?
) : TmdbType(), IPerson, MovieTvPersonListResult

data class Genre(
    val id: Int?,
    val name: String?
) : TmdbType()

data class CollectionInfo(
    val id: Int?,
    val name: String?,
    val originalName: String?,
    val overview: String?,
    val originalLanguage: LanguageCode?,
    val posterPath: String?,
    val backdropPath: String?,
    val adult: Boolean?
) : TmdbType()

data class CompanyInfo(
    val id: Int?,
    val logoPath: String?,
    val name: String?,
    val originCountry: CountryCode?
) : TmdbType()

data class CreditListResult(
    val castId: Int?,
    override val character: String?,
    val creditId: String?,
    override val department: String?,
    override val gender: Gender?,
    override val id: Int?,
    override val job: String?,
    override val name: String?,
    val order: Int?,
    override val profilePath: String?
) : TmdbType(), ICastMember, ICrewMember

data class ImageListResult(
    val aspectRatio: Double?,
    val filePath: String?,
    val fileType: String?,
    val height: Int?,
    val id: String?,
    val language: LanguageCode?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
) : TmdbType()

data class VideoListResult(
    val id: String?,
    val language: LanguageCode?,
    val country: CountryCode?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: VideoType?
) : TmdbType()

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
        val homepage: String?
    ) : TmdbType()
}

data class PaginatedListResults<out T : Any>(
    val id: Int?,
    override val page: Int,
    override val results: List<T> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<T>

data class PaginatedMovieListResultsWithDates(
    val id: Int?,
    override val page: Int?,
    override val results: List<MovieListResult> = emptyList(),
    val dates: Dates?,
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<MovieListResult> {
    data class Dates(
        val maximum: LocalDate?,
        val minimum: LocalDate?
    ) : TmdbType()
}

data class CastMember(
    val id: Int?,
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
    val adult: Boolean?
) : TmdbType()

data class CrewMember(
    val id: Int?,
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
) : TmdbType()

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
    `Behind the Scenes`
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
