package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.ItemStatus
import io.v47.tmdb.model.ListDetails
import io.v47.tmdb.utils.urlEncode

class ListApi(private val httpExecutor: HttpExecutor) {
    /**
     * Get the details of a list
     *
     * @param listId The id of the list
     * @param language A language code
     */
    fun details(listId: String, language: LocaleCode? = null) =
        httpExecutor.execute(
            get<ListDetails>("/list/${listId.urlEncode()}") {
                language?.let { queryArg("language", it.toString()) }
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
            get<ItemStatus>("/list/${listId.urlEncode()}/item_status") {
                queryArg("movie_id", movieId)
            }
        )
}
