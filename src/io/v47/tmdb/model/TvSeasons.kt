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

// @V3("/tv/{tv_id}/season/{season_number}")
data class TvSeasonDetails(
    val airDate: LocalDate?,
    val episodes: List<TvEpisodeDetails> = emptyList(),
    val name: String?,
    val overview: String?,
    val id: Int?,
    val posterPath: String?,
    val seasonNumber: Int?,
    val showId: Int?,
    val episodeCount: Int?,

    // append to response
    val changes: TvSeasonChanges?,
    val credits: TvSeasonCredits?,
    val externalIds: TvSeasonExternalIds?,
    val images: TvSeasonImages?,
    val videos: TvSeasonVideos?
) : TmdbType("_id")

// @V3("/tv/season/{season_id}/changes")
data class TvSeasonChanges(val changes: List<Change> = emptyList()) : TmdbType() {
    data class Change(
        val key: String?,
        val items: List<ChangeItem> = emptyList()
    ) : TmdbType()

    data class ChangeItem(
        val id: String?,
        val action: String?,
        val time: String?,
        val value: ChangeValue?,
        val language: LanguageCode?,
        val originalValue: String?
    ) : TmdbType()

    data class ChangeValue(
        val episodeId: Int?,
        val episodeNumber: Int?
    ) : TmdbType()
}

// @V3("/tv/{tv_id}/season/{season_number}/credits")
data class TvSeasonCredits(
    val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList()
) : TmdbType()


// @V3("/tv/{tv_id}/season/{season_number}/external_ids")
data class TvSeasonExternalIds(
    val id: Int?,
    val freebaseMid: String?,
    val freebaseId: String?,
    val tvdbId: Int?,
    val tvrageId: Int?
) : TmdbType()

// @V3("/tv/{tv_id}/season/{season_number}/images")
data class TvSeasonImages(
    val id: Int?,
    val posters: List<ImageListResult> = emptyList()
) : TmdbType()

// @V3("/tv/{tv_id}/season/{season_number}/videos")
data class TvSeasonVideos(
    val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType()
