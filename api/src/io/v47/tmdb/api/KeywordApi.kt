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
package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Keyword
import io.v47.tmdb.model.KeywordMovies

class KeywordApi internal constructor(private val httpExecutor: HttpExecutor) {
    fun details(keywordId: Int) = httpExecutor.execute(get<Keyword>("/keyword/$keywordId"))

    /**
     * Get the movies that belong to a keyword.
     *
     * We __highly recommend__ using [DiscoverApi.movies]  instead of this method as it
     * is much more flexible
     *
     * @param language A language code
     * @param includeAdult Choose whether to include adult (pornography) content in the results
     */
    fun movies(keywordId: Int, page: Int? = null, language: LocaleCode? = null, includeAdult: Boolean? = null) =
        httpExecutor.execute(
            getWithPageAndLanguage<KeywordMovies>("/keyword/{keywordId}/movies", page, language) {
                pathVar("keywordId", keywordId)
                includeAdult?.let { queryArg("include_adult", it) }
            }
        )
}
