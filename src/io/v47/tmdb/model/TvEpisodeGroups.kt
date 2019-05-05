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

// @V3("/tv/episode_group/{id}")
data class TvEpisodeGroupDetails(
    val id: String?,
    val name: String?,
    val type: TvEpisodeGroupType?,
    val description: String?,
    val episodeCount: Int?,
    val groupCount: Int?,
    val groups: List<TvEpisodeGroup> = emptyList(),
    val network: Network?
) : TmdbType() {
    data class TvEpisodeGroup(
        val id: String?,
        val name: String?,
        val order: Int?,
        val locked: Boolean?,
        val episodes: List<TvEpisodeDetails> = emptyList()
    ) : TmdbType()
}

enum class TvEpisodeGroupType {
    @Suppress("EnumEntryName")
    @Deprecated("Not a valid value")
    /** @suppress */
    __Dummy__,
    OriginalAirDate,
    Absolute,
    Dvd,
    Digital,
    StoryArc,
    Production,
    Tv
}
