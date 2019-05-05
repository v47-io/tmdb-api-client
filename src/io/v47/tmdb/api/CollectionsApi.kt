package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.CollectionDetails
import io.v47.tmdb.model.CollectionImages
import io.v47.tmdb.model.CollectionTranslations

class CollectionsApi(private val httpExecutor: HttpExecutor) {
    /**
     * Get collection details by id
     *
     * @param collectionId The id of the collection
     * @param language A language code
     */
    fun getDetails(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            get<CollectionDetails>("/collection/$collectionId") {
                language?.let { queryArg("language", it) }
            }
        )

    /**
     * Get the images for a collection by id
     *
     * @param collectionId The id of the collection
     * @param language A language code
     */
    fun getImages(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            get<CollectionImages>("/collection/$collectionId/images") {
                language?.let { queryArg("language", it) }
            }
        )

    /**
     * Get the list translations for a collection by id
     *
     * @param collectionId The id of the collection
     */
    fun getTranslations(collectionId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            get<CollectionTranslations>("/collection/$collectionId/translations") {
                language?.let { queryArg("language", it) }
            }
        )
}
