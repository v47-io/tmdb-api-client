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
    val instagramId: String?
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
    ) : TmdbType()
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
            val originalValue: Map<String, Any> = emptyMap()
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
        val gender: Gender?,
        val popularity: Double?
    ) : TmdbType(), TmdbIntId
}
