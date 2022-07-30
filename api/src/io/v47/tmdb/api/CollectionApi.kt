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
import io.v47.tmdb.model.CollectionDetails
import io.v47.tmdb.model.CollectionImages
import io.v47.tmdb.model.CollectionTranslations

class CollectionApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get collection details by id
     *
     * @param collectionId The id of the collection
     * @param language A language code
     */
    fun details(collectionId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<CollectionDetails>("/collection/{collectionId}", language) {
            pathVar("collectionId", collectionId)
        }


    /**
     * Get the images for a collection by id
     *
     * @param collectionId The id of the collection
     * @param language A language code
     */
    fun images(collectionId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<CollectionImages>("/collection/{collectionId}/images", language) {
            pathVar("collectionId", collectionId)
        }


    /**
     * Get the list translations for a collection by id
     *
     * @param collectionId The id of the collection
     */
    fun translations(collectionId: Int, language: LocaleCode? = null) =
        http.getWithLanguage<CollectionTranslations>(
            "/collection/{collectionId}/translations",
            language
        ) {
            pathVar("collectionId", collectionId)
        }

}
