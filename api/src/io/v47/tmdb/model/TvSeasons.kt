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

import com.neovisionaries.i18n.LanguageCode
import java.time.LocalDate

data class TvSeasonDetails(
    val airDate: LocalDate?,
    val episodes: List<TvEpisodeDetails> = emptyList(),
    val name: String?,
    val overview: String?,
    override val id: Int?,
    val mediaType: MediaType?,
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
        val value: Any?,
        val language: LanguageCode?,
        val originalValue: Any?
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
