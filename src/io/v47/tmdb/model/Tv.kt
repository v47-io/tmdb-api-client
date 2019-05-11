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

// @V3("/tv/{tv_id}")
data class TvShowDetails(
    val backdropPath: String?,
    val createdBy: List<PersonListResult> = emptyList(),
    val episodeRunTime: List<Int> = emptyList(),
    val firstAirDate: LocalDate?,
    val genres: List<Genre> = emptyList(),
    val homepage: String?,
    val id: Int?,
    val inProduction: Boolean?,
    val languages: List<LanguageCode> = emptyList(),
    val lastAirDate: LocalDate?,
    val lastEpisodeToAir: TvEpisodeDetails?,
    val nextEpisodeToAir: TvEpisodeDetails?,
    val name: String?,
    val networks: List<Network> = emptyList(),
    val numberOfEpisodes: Int?,
    val numberOfSeasons: Int?,
    val originCountry: List<CountryCode> = emptyList(),
    val originalLanguage: LanguageCode?,
    val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val productionCompanies: List<CompanyInfo> = emptyList(),
    val seasons: List<TvSeasonDetails> = emptyList(),
    val status: TvShowStatus?,
    val type: TvShowType?,
    val voteAverage: Double?,
    val voteCount: Int?,

    //append to response
    val alternativeTitles: TvShowAlternativeTitles?,
    val changes: TvShowChanges?,
    val contentRatings: TvShowContentRatings?,
    val credits: TvShowCredits?,
    val externalIds: TvShowExternalIds?,
    val images: TvShowImages?,
    val keywords: TvShowKeywords?,
    val recommendations: PaginatedListResults<TvListResult>?,
    val screenedTheatrically: TvShowScreenedTheatrically?,
    val similar: PaginatedListResults<TvListResult>?,
    val translations: TvShowTranslations?,
    val videos: TvShowVideos?
) : TmdbType()

// @V3("/tv/{tv_id}/alternative_titles")
data class TvShowAlternativeTitles(
    val id: Int?,
    val results: List<Title> = emptyList()
) : TmdbType()

// @V3("/tv/{tv_id}/changes")
data class TvShowChanges(val changes: List<Change> = emptyList()) : TmdbType() {
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

// @V3("/tv/{tv_id}/content_ratings")
data class TvShowContentRatings(
    val id: Int?,
    val results: List<Rating> = emptyList()
) : TmdbType() {
    data class Rating(
        val rating: String?,
        val country: CountryCode?
    ) : TmdbType()
}

// @V3("/tv/{tv_id}/credits")
data class TvShowCredits(
    val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList()
) : TmdbType()

// @V3("/tv/{tv_id}/episode_groups")
data class TvShowEpisodeGroups(
    val id: Int?,
    val results: List<TvShowEpisodeGroup> = emptyList()
) : TmdbType() {
    data class TvShowEpisodeGroup(
        val id: String?,
        val name: String?,
        val type: TvEpisodeGroupType?,
        val description: String?,
        val episodeCount: Int?,
        val groupCount: Int?,
        val network: Network?
    ) : TmdbType()
}

// @V3("/tv/{tv_id}/external_ids")
data class TvShowExternalIds(
    val id: Int?,
    val imdbId: String?,
    val freebaseMid: String?,
    val freebaseId: String?,
    val tvdbId: Int?,
    val tvrageId: Int?,
    val facebookId: String?,
    val instagramId: String?,
    val twitterId: String?
) : TmdbType()

// @V3("/tv/{tv_id}/images")
data class TvShowImages(
    val id: Int?,
    val backdrops: List<ImageListResult> = emptyList(),
    val posters: List<ImageListResult> = emptyList()
) : TmdbType()

// @V3("/tv/{tv_id}/keywords")
data class TvShowKeywords(
    val id: Int?,
    val results: List<Keyword> = emptyList()
) : TmdbType()

data class TvShowReview(
    val id: String?,
    val url: String?,
    val author: String?,
    val content: String?
) : TmdbType()

// @V3("/tv/{tv_id}/screened_theatrically")
data class TvShowScreenedTheatrically(
    val id: Int?,
    val results: List<ScreenedResult> = emptyList()
) : TmdbType() {
    data class ScreenedResult(
        val id: Int?,
        val seasonNumber: Int?,
        val episodeNumber: Int?
    ) : TmdbType()
}

// @V3("/tv/{tv_id}/translations")
data class TvShowTranslations(
    val id: Int?,
    val translations: List<TranslationListResult> = emptyList()
) : TmdbType()

// @V3("/tv/{tv_id}/videos")
data class TvShowVideos(
    val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType()
