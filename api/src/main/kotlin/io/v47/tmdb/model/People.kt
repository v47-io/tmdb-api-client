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

data class PersonTaggedImages(
    override val id: Int?,
    override val page: Int?,
    override val results: List<TaggedImage> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<PersonTaggedImages.TaggedImage> {
    data class TaggedImage(
        override val id: String?,
        val aspectRatio: Double?,
        val filePath: String?,
        val height: Int?,
        val language: LanguageCode?,
        val voteAverage: Double?,
        val voteCount: Int?,
        val width: Int?,
        val imageType: String?,
        val media: MovieTvPersonListResult?,
        val mediaType: MediaType?
    ) : TmdbType(), TmdbStringId
}

data class PersonTranslations(
    override val id: Int?,
    val translations: List<PersonTranslation> = emptyList()
) : TmdbType(), TmdbIntId {
    data class PersonTranslation(
        val language: LanguageCode?,
        val country: CountryCode?,
        val name: String?,
        val data: PersonTranslationData?,
        val englishName: String?
    ) : TmdbType() {
        data class PersonTranslationData(val biography: String?) : TmdbType()
    }
}

data class PersonChanges(val changes: List<PersonChange> = emptyList()) : TmdbType() {
    data class PersonChange(
        val key: String?,
        val items: List<PersonChangeItem> = emptyList()
    ) : TmdbType() {
        data class PersonChangeItem(
            override val id: String?,
            val action: String?,
            val time: String?,
            val language: LanguageCode?,
            val country: CountryCode?,
            val value: Any?,
            val originalValue: Any?
        ) : TmdbType(), TmdbStringId
    }
}

data class PeoplePopular(
    override val page: Int?,
    override val results: List<PopularPerson>,
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<PeoplePopular.PopularPerson> {
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
