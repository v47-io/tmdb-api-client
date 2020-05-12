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
package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.ItemStatus
import io.v47.tmdb.model.ListDetails

class ListApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the details of a list
     *
     * @param listId The id of the list
     * @param language A language code
     */
    fun details(listId: String, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<ListDetails>("/list/{listId}", language) {
                pathVar("listId", listId)
            }
        )

    /**
     * Check if a movie has already been added to the list
     *
     * @param listId The id of the list
     * @param movieId The id of the movie
     */
    fun checkItemStatus(listId: String, movieId: Int) =
        httpExecutor.execute(
            get<ItemStatus>("/list/{listId}/item_status") {
                pathVar("listId", listId)
                queryArg("movie_id", movieId)
            }
        )
}
