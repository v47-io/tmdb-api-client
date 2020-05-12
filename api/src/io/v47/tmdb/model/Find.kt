/**
 * Copyright 2020 The tmdb-api-v2 Authors
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

data class Find(
    val movieResults: List<MovieListResult> = emptyList(),
    val personResults: List<PersonListResult> = emptyList(),
    val tvResults: List<TvListResult> = emptyList(),
    val tvEpisodeResults: List<TvEpisodeDetails> = emptyList(),
    val tvSeasonResults: List<TvSeasonDetails> = emptyList()
) : TmdbType()
