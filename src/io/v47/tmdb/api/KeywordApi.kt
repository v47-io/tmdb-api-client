package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.Keyword
import io.v47.tmdb.model.KeywordMovies
import io.v47.tmdb.utils.checkPage

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
            get<KeywordMovies>("/keyword/$keywordId/movies") {
                page?.let {
                    checkPage(it)
                    queryArg("page", it)
                }

                language?.let { queryArg("language", it.toString()) }
                includeAdult?.let { queryArg("include_adult", it) }
            }
        )
}
