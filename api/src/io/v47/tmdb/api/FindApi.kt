/**
 * The Clear BSD License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
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
package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Find

class FindApi internal constructor(private val http: HttpExecutor) {
    /**
     * The find method makes it easy to search for objects in our database by an external id.
     * For example, an IMDb ID.
     *
     * This method will search all objects (movies, TV shows and people) and return the
     * results in a single response.
     *
     * See the support matrix [here](https://developers.themoviedb.org/3/find/find-by-id)
     *
     * @param externalId The id to look up in an external source
     * @param source An external source the specified id belongs to
     * @param language A language code
     */
    fun byId(externalId: Any, source: ExternalSource, language: LocaleCode? = null) =
        http.getWithLanguage<Find>("/find/{externalId}", language) {
            pathVar("externalId", externalId)
            queryArg("external_source", source.value)
        }


    enum class ExternalSource(internal val value: String) {
        IMDb("imdb_id"),
        TVDB("tvdb_id"),

        @Deprecated("Defunct or no longer available as a service")
        Freebase("freebase_id"),

        @Deprecated("Defunct or no longer available as a service")
        FreebaseM("freebase_mid"),

        @Deprecated("Defunct or no longer available as a service")
        TVRage("tvrage_id"),
        Facebook("facebook_id"),
        Instagram("instagram_id"),
        Twitter("twitter_id")
    }
}
