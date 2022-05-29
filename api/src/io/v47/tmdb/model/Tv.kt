/**
 * Copyright 2022 The tmdb-api-v2 Authors
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

data class TvShowDetails(
    val backdropPath: String?,
    val createdBy: List<PersonListResult> = emptyList(),
    val episodeRunTime: List<Int> = emptyList(),
    val firstAirDate: LocalDate?,
    val genres: List<Genre> = emptyList(),
    val homepage: String?,
    override val id: Int?,
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
) : TmdbType(), TmdbIntId

data class TvShowAlternativeTitles(
    override val id: Int?,
    val results: List<Title> = emptyList()
) : TmdbType(), TmdbIntId

data class TvShowChanges(val changes: List<Change> = emptyList()) : TmdbType() {
    data class Change(
        val key: String?,
        val items: List<ChangeItem> = emptyList()
    ) : TmdbType()

    data class ChangeItem(
        override val id: String?,
        val action: String?,
        val time: String?,
        val language: LanguageCode?,
        val value: Any?,
        val originalValue: Any?
    ) : TmdbType(), TmdbStringId
}

data class TvShowContentRatings(
    override val id: Int?,
    val results: List<Rating> = emptyList()
) : TmdbType(), TmdbIntId {
    data class Rating(
        val rating: String?,
        val country: CountryCode?
    ) : TmdbType()
}

data class TvShowCredits(
    override val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class TvShowEpisodeGroups(
    override val id: Int?,
    val results: List<TvShowEpisodeGroup> = emptyList()
) : TmdbType(), TmdbIntId {
    data class TvShowEpisodeGroup(
        override val id: String?,
        val name: String?,
        val type: TvEpisodeGroupType?,
        val description: String?,
        val episodeCount: Int?,
        val groupCount: Int?,
        val network: Network?
    ) : TmdbType(), TmdbStringId
}

data class TvShowExternalIds(
    override val id: Int?,
    val imdbId: String?,
    val freebaseMid: String?,
    val freebaseId: String?,
    val tvdbId: Int?,
    val tvrageId: Int?,
    val facebookId: String?,
    val instagramId: String?,
    val twitterId: String?
) : TmdbType(), TmdbIntId

data class TvShowImages(
    override val id: Int?,
    val backdrops: List<ImageListResult> = emptyList(),
    val posters: List<ImageListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class TvShowKeywords(
    override val id: Int?,
    val results: List<Keyword> = emptyList()
) : TmdbType(), TmdbIntId

data class TvShowReview(
    override val id: String?,
    val url: String?,
    val author: String?,
    val content: String?
) : TmdbType(), TmdbStringId

data class TvShowScreenedTheatrically(
    override val id: Int?,
    val results: List<ScreenedResult> = emptyList()
) : TmdbType(), TmdbIntId {
    data class ScreenedResult(
        override val id: Int?,
        val seasonNumber: Int?,
        val episodeNumber: Int?
    ) : TmdbType(), TmdbIntId
}

data class TvShowTranslations(
    override val id: Int?,
    val translations: List<TranslationListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class TvShowVideos(
    override val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType(), TmdbIntId
