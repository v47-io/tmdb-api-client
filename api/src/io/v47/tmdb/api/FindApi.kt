/**
 * Copyright 2022 The tmdb-api-client Authors
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
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Find

class FindApi internal constructor(private val httpExecutor: HttpExecutor) {
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
        httpExecutor.execute(
            getWithLanguage<Find>("/find/{externalId}", language) {
                pathVar("externalId", externalId)
                queryArg("external_source", source.value)
            }
        )

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
