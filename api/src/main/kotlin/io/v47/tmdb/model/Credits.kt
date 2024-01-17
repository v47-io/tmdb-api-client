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

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import java.io.Serializable
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

interface CreditMedia : TmdbIntId, Serializable {
    val adult: Boolean?
    val backdropPath: String?
    val character: String?
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

data class CreditMediaMovie(
    override val adult: Boolean?,
    override val backdropPath: String?,
    override val character: String?,
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
) : CreditMedia, TmdbType()

data class CreditMediaTv(
    override val adult: Boolean?,
    override val backdropPath: String?,
    override val character: String?,
    val episodes: List<CreditMediaTvEpisode> = emptyList(),
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
    val runtime: Int?,
    val seasonNumber: Int?,
    val showId: Int?,
    val stillPath: String?,
    val voteAverage: Double?,
    val voteCount: Long?,
    val mediaType: MediaType?,
    val episodeType: TvEpisodeType?
) : TmdbType(), TmdbIntId

data class CreditMediaTvSeason(
    val airDate: LocalDate?,
    val episodeCount: Int?,
    override val id: Int?,
    val name: String?,
    val overview: String?,
    val posterPath: String?,
    val seasonNumber: Int?,
    val showId: Int?,
    val mediaType: MediaType?,
    val voteAverage: Double?
) : TmdbType(), TmdbIntId

data class CreditPerson(
    val adult: Boolean?,
    val gender: Gender?,
    val name: String?,
    override val id: Int?,
    val knownFor: List<CreditPersonKnownFor> = emptyList(),
    val knownForDepartment: String?,
    val mediaType: MediaType?,
    val originalName: String?,
    val profilePath: String?,
    val popularity: Double?
) : TmdbType(), TmdbIntId

interface CreditPersonKnownFor : TmdbIntId, Serializable {
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
