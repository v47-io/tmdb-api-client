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
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.CollectionDetails
import io.v47.tmdb.model.CollectionImages
import io.v47.tmdb.model.CollectionTranslations

class CollectionApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get collection details by id
     *
     * @param collectionId The id of the collection
     * @param language A language code
     */
    fun details(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<CollectionDetails>("/collection/{collectionId}", language) {
                pathVar("collectionId", collectionId)
            }
        )

    /**
     * Get the images for a collection by id
     *
     * @param collectionId The id of the collection
     * @param language A language code
     */
    fun images(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<CollectionImages>("/collection/{collectionId}/images", language) {
                pathVar("collectionId", collectionId)
            }
        )

    /**
     * Get the list translations for a collection by id
     *
     * @param collectionId The id of the collection
     */
    fun translations(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<CollectionTranslations>("/collection/{collectionId}/translations", language) {
                pathVar("collectionId", collectionId)
            }
        )
}
