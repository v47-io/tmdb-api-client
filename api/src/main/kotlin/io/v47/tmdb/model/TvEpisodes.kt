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

import com.neovisionaries.i18n.LanguageCode
import java.time.LocalDate

data class TvEpisodeDetails(
    val airDate: LocalDate?,
    val crew: List<CreditListResult> = emptyList(),
    val episodeNumber: Int?,
    val guestStars: List<CreditListResult> = emptyList(),
    val name: String?,
    val overview: String?,
    override val id: Int?,
    val mediaType: MediaType?,
    val productionCode: String?,
    val runtime: Int?,
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
) : TmdbType(), TmdbIntId

data class TvEpisodeChanges(val changes: List<Change> = emptyList()) : TmdbType() {
    data class Change(
        val key: String?,
        val items: List<ChangeItem> = emptyList()
    ) : TmdbType()

    data class ChangeItem(
        override val id: String?,
        val action: String?,
        val time: String?,
        val value: Any?,
        val originalValue: Any?,
        val language: LanguageCode?
    ) : TmdbType(), TmdbStringId
}

data class TvEpisodeCredits(
    override val id: Int?,
    val cast: List<CreditListResult> = emptyList(),
    val crew: List<CreditListResult> = emptyList(),
    val guestStars: List<CreditListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class TvEpisodeExternalIds(
    override val id: Int?,
    val imdbId: String?,
    val freebaseMid: String?,
    val freebaseId: String?,
    val tvdbId: Int?,
    val tvrageId: Int?
) : TmdbType(), TmdbIntId

data class TvEpisodeImages(
    override val id: Int?,
    val stills: List<ImageListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class TvEpisodeTranslations(
    override val id: Int?,
    val translations: List<TranslationListResult> = emptyList()
) : TmdbType(), TmdbIntId

data class TvEpisodeVideos(
    override val id: Int?,
    val results: List<VideoListResult> = emptyList()
) : TmdbType(), TmdbIntId
