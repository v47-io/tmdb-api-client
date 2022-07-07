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
package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Keyword
import io.v47.tmdb.model.KeywordMovies

class KeywordApi internal constructor(private val http: HttpExecutor) {
    fun details(keywordId: Int) = http.get<Keyword>("/keyword/$keywordId")

    /**
     * Get the movies that belong to a keyword.
     *
     * We __highly recommend__ using [DiscoverApi.movies]  instead of this method as it
     * is much more flexible
     *
     * @param language A language code
     * @param includeAdult Choose whether to include adult (pornography) content in the results
     */
    fun movies(
        keywordId: Int,
        page: Int? = null,
        language: LocaleCode? = null,
        includeAdult: Boolean? = null
    ) =
        http.getWithPageAndLanguage<KeywordMovies>("/keyword/{keywordId}/movies", page, language) {
            pathVar("keywordId", keywordId)
            includeAdult?.let { queryArg("include_adult", it) }
        }

}
