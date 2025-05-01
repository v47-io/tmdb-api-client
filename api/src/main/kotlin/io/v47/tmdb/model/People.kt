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
data class PersonDetails(
    val adult: Boolean?,
    val alsoKnownAs: List<String> = emptyList(),
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    val gender: Gender?,
    val homepage: String?,
    override val id: Int?,
    val imdbId: String?,
    val name: String?,
    val placeOfBirth: String?,
    val popularity: Double?,
    val profilePath: String?,
    val knownForDepartment: String?,

    // append to response
    val movieCredits: PersonCredits?,
    val tvCredits: PersonCredits?,
    val combinedCredits: PersonCredits?,
    val externalIds: PersonExternalIds?,
    val images: PersonImages?,
    val taggedImages: PersonTaggedImages?,
    val translations: PersonTranslations?,
    val changes: PersonChanges?
) : TmdbType(), TmdbIntId

data class PersonCredits(
    override val id: Int?,
    val cast: List<CastMember> = emptyList(),
    val crew: List<CrewMember> = emptyList()
) : TmdbType(), TmdbIntId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PersonExternalIds(
    val imdbId: String?,
    val facebookId: String?,
    val freebaseMid: String?,
    val freebaseId: String?,
    val tvrageId: Int?,
    val twitterId: String?,
    override val id: Int?,
    val instagramId: String?,
    val youtubeId: String?,
    val tiktokId: String?,
    val wikidataId: String?
) : TmdbType(), TmdbIntId

data class PersonImages(
    override val id: Int?,
    val profiles: List<ImageListResult> = emptyList()
) : TmdbType(), TmdbIntId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PersonTaggedImages(
    override val id: Int?,
    override val page: Int?,
    override val results: List<TaggedImage> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<PersonTaggedImages.TaggedImage> {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class TaggedImage(
        override val id: String?,
        val aspectRatio: Double?,
        val filePath: String?,
        val height: Int?,
        @JsonProperty("iso_639_1")
        val language: LanguageCode?,
        val voteAverage: Double?,
        val voteCount: Int?,
        val width: Int?,
        val imageType: String?,
        val media: MovieTvPersonListResult?,
        val mediaType: MediaType?
    ) : TmdbType(), TmdbStringId
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PersonTranslations(
    override val id: Int?,
    val translations: List<PersonTranslation> = emptyList()
) : TmdbType(), TmdbIntId {
    data class PersonTranslation(
        @JsonProperty("iso_639_1")
        val language: LanguageCode?,
        @JsonProperty("iso_3166_1")
        val country: CountryCode?,
        val name: String?,
        val data: PersonTranslationData?,
        @JsonProperty("english_name")
        val englishName: String?
    ) : TmdbType() {
        data class PersonTranslationData(
            val primary: Boolean?,
            val name: String?,
            val biography: String?
        ) : TmdbType()
    }
}

data class PersonChanges(val changes: List<PersonChange> = emptyList()) : TmdbType() {
    data class PersonChange(
        val key: String?,
        val items: List<PersonChangeItem> = emptyList()
    ) : TmdbType() {
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
        data class PersonChangeItem(
            override val id: String?,
            val action: String?,
            val time: String?,
            @JsonProperty("iso_639_1")
            val language: LanguageCode?,
            @JsonProperty("iso_3166_1")
            val country: CountryCode?,
            val value: Any?,
            val originalValue: Any?
        ) : TmdbType(), TmdbStringId
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PeoplePopular(
    override val page: Int?,
    override val results: List<PopularPerson>,
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<PeoplePopular.PopularPerson> {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class PopularPerson(
        val profilePath: String?,
        val adult: Boolean?,
        override val id: Int?,
        val knownFor: List<MovieTvPersonListResult> = emptyList(),
        val knownForDepartment: String?,
        val name: String?,
        val originalName: String?,
        val gender: Gender?,
        val popularity: Double?
    ) : TmdbType(), TmdbIntId
}

interface MovieTvPersonListResult : TmdbIntId {
    override val id: Int?
    val mediaType: MediaType?
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

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
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

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CastMember(
    override val id: Int?,
    @JsonDeserialize(using = OriginalLanguageDeserializer::class)
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

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CrewMember(
    override val id: Int?,
    val department: String?,
    @JsonDeserialize(using = OriginalLanguageDeserializer::class)
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
