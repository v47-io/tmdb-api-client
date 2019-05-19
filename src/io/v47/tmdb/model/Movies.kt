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
import java.time.LocalDate

data class MovieDetails(
    val adult: Boolean?,
    val backdropPath: String?,
    val belongsToCollection: CollectionInfo?,
    val budget: Int?,
    val genres: List<Genre> = emptyList(),
    val homepage: String?,
    val id: Int?,
    val imdbId: String?,
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
) : TmdbType()

data class MovieAlternativeTitles(
    val id: Int?,
    val titles: List<Title> = emptyList()
) : TmdbType()

data class MovieChanges(
    override val page: Int?,
    override val results: List<Change> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<MovieChanges.Change> {
    data class Change(
        val key: String?,
        val items: List<ChangeItem> = emptyList()
    ) : TmdbType()

    data class ChangeItem(
        val id: String?,
        val action: String?,
        val time: String?,
        val language: LanguageCode?,
        val value: Any?,
        val originalValue: Any?
    ) : TmdbType()
}

data class MovieCredits(
    val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList()
) : TmdbType()

data class MovieExternalIds(
    val id: Int?,
    val imdbId: String?,
    val facebookId: String?,
    val instagramId: String?,
    val twitterId: String?
) : TmdbType()

data class MovieImages(
    val id: Int?,
    val backdrops: List<ImageListResult> = emptyList(),
    val posters: List<ImageListResult> = emptyList()
) : TmdbType()

data class MovieKeywords(
    val id: Int?,
    val keywords: List<Keyword> = emptyList()
) : TmdbType()

data class MovieReleaseDates(
    val id: Int?,
    val results: List<ReleaseDates> = emptyList()
) : TmdbType() {
    data class ReleaseDates(
        val country: CountryCode?,
        val releaseDates: List<MovieReleaseInfo> = emptyList()
    ) : TmdbType() {
        data class MovieReleaseInfo(
            val certification: String?,
            val language: LanguageCode?,
            val note: String?,
            val releaseDate: LocalDate?,
            val type: Release?
        ) : TmdbType()
    }
}

data class MovieVideos(
    val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType()

data class MovieTranslations(
    val id: Int?,
    val translations: List<TranslationListResult> = emptyList()
) : TmdbType()

data class MovieReviews(
    val id: Int?,
    override val page: Int?,
    override val results: List<ReviewDetails> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<ReviewDetails>

data class MovieLists(
    val id: Int?,
    override val page: Int?,
    override val results: List<ListDetails> = emptyList(),
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), Paginated<ListDetails>
