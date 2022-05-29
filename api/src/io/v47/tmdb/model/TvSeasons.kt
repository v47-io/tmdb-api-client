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

import com.neovisionaries.i18n.LanguageCode
import java.time.LocalDate

data class TvSeasonDetails(
    val airDate: LocalDate?,
    val episodes: List<TvEpisodeDetails> = emptyList(),
    val name: String?,
    val overview: String?,
    override val id: Int?,
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
) : TmdbType("_id"), TmdbIntId

data class TvSeasonChanges(val changes: List<Change> = emptyList()) : TmdbType() {
    data class Change(
        val key: String?,
        val items: List<ChangeItem> = emptyList()
    ) : TmdbType()

    data class ChangeItem(
        override val id: String?,
        val action: String?,
        val time: String?,
        val value: ChangeValue?,
        val language: LanguageCode?,
        val originalValue: String?
    ) : TmdbType(), TmdbStringId

    data class ChangeValue(
        val episodeId: Int?,
        val episodeNumber: Int?
    ) : TmdbType()
}

data class TvSeasonCredits(
    override val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList()
) : TmdbType(), TmdbIntId


data class TvSeasonExternalIds(
    override val id: Int?,
    val freebaseMid: String?,
    val freebaseId: String?,
    val tvdbId: Int?,
    val tvrageId: Int?
) : TmdbType(), TmdbIntId

data class TvSeasonImages(
    override val id: Int?,
    val posters: List<ImageListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class TvSeasonVideos(
    override val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType(), TmdbIntId
