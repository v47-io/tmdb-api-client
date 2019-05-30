package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.getWithLanguage
import io.v47.tmdb.model.TvEpisodeGroupDetails

class TvEpisodeGroupsApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the details of a TV episode group
     *
     * @param id The id of the TV episode group
     * @param language A language code
     */
    fun details(id: String, language: LocaleCode? = null) =
        httpExecutor.execute(getWithLanguage<TvEpisodeGroupDetails>("/tv/episode_group/$id", language))
}
