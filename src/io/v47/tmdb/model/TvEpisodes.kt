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

import com.neovisionaries.i18n.LanguageCode
import java.time.LocalDate

data class TvEpisodeDetails(
    val airDate: LocalDate?,
    val crew: List<CreditListResult> = emptyList(),
    val episodeNumber: Int?,
    val guestStars: List<CreditListResult> = emptyList(),
    val name: String?,
    val overview: String?,
    val id: Int?,
    val productionCode: String?,
    val seasonNumber: String?,
    val showId: Int?,
    val stillPath: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val order: Int?,

    // append to response
    val changes: TvEpisodeChanges?,
    val credits: TvEpisodeCredits?,
    val externalIds: TvEpisodeExternalIds?,
    val images: TvEpisodeImages?,
    val translations: TvEpisodeTranslations?,
    val videos: TvEpisodeVideos?
) : TmdbType()

data class TvEpisodeChanges(val changes: List<Change> = emptyList()) : TmdbType() {
    data class Change(
        val key: String?,
        val items: List<ChangeItem> = emptyList()
    ) : TmdbType()

    data class ChangeItem(
        val id: String?,
        val action: String?,
        val time: String?,
        val value: String?,
        val originalValue: String?,
        val language: LanguageCode?
    ) : TmdbType()
}

data class TvEpisodeCredits(
    val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList(),
    val guestStars: List<CreditListResult> = emptyList()
) : TmdbType()

data class TvEpisodeExternalIds(
    val id: Int?,
    val imdbId: String?,
    val freebaseMid: String?,
    val freebaseId: String?,
    val tvdbId: Int?,
    val tvrageId: Int?
) : TmdbType()

data class TvEpisodeImages(
    val id: Int?,
    val stills: List<ImageListResult> = emptyList()
) : TmdbType()

data class TvEpisodeTranslations(
    val id: Int?,
    val translations: List<TranslationListResult> = emptyList()
) : TmdbType()

data class TvEpisodeVideos(
    val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType()
