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

data class Keyword(
    override val id: Int?,
    val name: String?
) : TmdbType(), TmdbIntId

data class KeywordMovies(
    override val id: Int?,
    override val page: Int?,
    override val results: List<MovieListResult>,
    override val totalPages: Int?,
    override val totalResults: Int?
) : TmdbType(), TmdbIntId, Paginated<MovieListResult>
