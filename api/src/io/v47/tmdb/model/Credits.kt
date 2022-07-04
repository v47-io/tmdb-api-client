/**
 * Copyright 2022 The tmdb-api-client Authors
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

data class Credits(
    val creditType: CreditType?,
    val department: String?,
    val job: String?,
    val media: CreditMedia?,
    val mediaType: MediaType,
    override val id: String?,
    val person: CreditPerson?
) : TmdbType(), TmdbStringId

interface CreditMedia : TmdbIntId {
    val adult: Boolean?
    val backdropPath: String?
    val character: String?
    val genreIds: List<Int>
    override val id: Int?
    val originalLanguage: LanguageCode?
    val overview: String?
    val popularity: Double?
    val posterPath: String?
    val voteAverage: Double?
    val voteCount: Long?
}

data class CreditMediaMovie(
    override val adult: Boolean?,
    override val backdropPath: String?,
    override val character: String?,
    override val genreIds: List<Int> = emptyList(),
    override val id: Int?,
    override val originalLanguage: LanguageCode?,
    val originalTitle: String?,
    override val overview: String?,
    override val popularity: Double?,
    override val posterPath: String?,
    val releaseDate: LocalDate?,
    val title: String?,
    val video: Boolean?,
    override val voteAverage: Double?,
    override val voteCount: Long?
) : CreditMedia, TmdbType()

data class CreditMediaTv(
    override val adult: Boolean?,
    override val backdropPath: String?,
    override val character: String?,
    val episodes: List<CreditMediaTvEpisode> = emptyList(),
    val firstAirDate: LocalDate?,
    override val genreIds: List<Int> = emptyList(),
    override val id: Int?,
    val name: String?,
    val originCountry: List<CountryCode> = emptyList(),
    override val originalLanguage: LanguageCode?,
    val originalName: String?,
    override val overview: String?,
    override val popularity: Double?,
    override val posterPath: String?,
    val seasons: List<CreditMediaTvSeason> = emptyList(),
    override val voteAverage: Double?,
    override val voteCount: Long?
) : CreditMedia, TmdbType()

data class CreditMediaTvEpisode(
    val airDate: LocalDate?,
    val episodeNumber: Int?,
    override val id: Int?,
    val name: String?,
    val overview: String?,
    val productionCode: String?,
    val seasonNumber: Int?,
    val showId: Int?,
    val stillPath: String?,
    val voteAverage: Double?,
    val voteCount: Long?
) : TmdbType(), TmdbIntId

data class CreditMediaTvSeason(
    val airDate: LocalDate?,
    val episodeCount: Int?,
    override val id: Int?,
    val name: String?,
    val overview: String?,
    val posterPath: String?,
    val seasonNumber: Int?,
    val showId: Int?
) : TmdbType(), TmdbIntId

data class CreditPerson(
    val adult: Boolean?,
    val gender: Gender?,
    val name: String?,
    override val id: Int?,
    val knownFor: List<CreditPersonKnownFor> = emptyList(),
    val knownForDepartment: String?,
    val profilePath: String?,
    val popularity: Double?
) : TmdbType(), TmdbIntId

interface CreditPersonKnownFor : TmdbIntId {
    val adult: Boolean?
    val backdropPath: String?
    val genreIds: List<Int>
    override val id: Int?
    val mediaType: MediaType?
    val originalLanguage: LanguageCode?
    val overview: String?
    val popularity: Double?
    val posterPath: String?
    val voteAverage: Double?
    val voteCount: Long?
}

data class CreditPersonKnownForMovie(
    override val adult: Boolean?,
    override val backdropPath: String?,
    override val genreIds: List<Int> = emptyList(),
    override val id: Int?,
    override val mediaType: MediaType?,
    override val originalLanguage: LanguageCode?,
    val originalTitle: String?,
    override val overview: String?,
    override val popularity: Double?,
    override val posterPath: String?,
    val releaseDate: LocalDate?,
    val title: String?,
    val video: Boolean?,
    override val voteAverage: Double?,
    override val voteCount: Long?
) : CreditPersonKnownFor, TmdbType()

data class CreditPersonKnownForTv(
    override val adult: Boolean?,
    override val backdropPath: String?,
    val firstAirDate: LocalDate?,
    override val genreIds: List<Int> = emptyList(),
    override val id: Int?,
    override val mediaType: MediaType?,
    val name: String?,
    val originCountry: List<CountryCode> = emptyList(),
    override val originalLanguage: LanguageCode?,
    val originalName: String?,
    override val overview: String?,
    override val popularity: Double?,
    override val posterPath: String?,
    override val voteAverage: Double?,
    override val voteCount: Long?
) : CreditPersonKnownFor, TmdbType()
