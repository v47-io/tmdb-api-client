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
        httpExecutor.execute(getWithLanguage<CollectionDetails>("/collection/$collectionId", language))

    /**
     * Get the images for a collection by id
     *
     * @param collectionId The id of the collection
     * @param language A language code
     */
    fun images(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(getWithLanguage<CollectionImages>("/collection/$collectionId/images", language))

    /**
     * Get the list translations for a collection by id
     *
     * @param collectionId The id of the collection
     */
    fun translations(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<CollectionTranslations>("/collection/$collectionId/translations", language)
        )
}
