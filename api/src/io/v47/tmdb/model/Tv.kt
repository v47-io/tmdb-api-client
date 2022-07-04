/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.model

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import java.time.Instant
import java.time.LocalDate

data class TvShowDetails(
    val adult: Boolean?,
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
    val productionCountries: List<Country> = emptyList(),
    val seasons: List<TvSeasonDetails> = emptyList(),
    val spokenLanguages: List<Language> = emptyList(),
    val status: TvShowStatus?,
    val tagline: String?,
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
    val posters: List<ImageListResult> = emptyList(),
    val logos: List<ImageListResult> = emptyList(),
) : TmdbType(), TmdbIntId

data class TvShowKeywords(
    override val id: Int?,
    val results: List<Keyword> = emptyList()
) : TmdbType(), TmdbIntId

data class TvShowReview(
    override val id: String?,
    val url: String?,
    val author: String?,
    val authorDetails: ReviewAuthorDetails?,
    val content: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
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
