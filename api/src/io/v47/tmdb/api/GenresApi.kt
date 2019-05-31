package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.GenreList

class GenresApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the list of official genres for movies
     *
     * @param language A language code
     */
    fun forMovies(language: LocaleCode? = null) =
        httpExecutor.execute(getWithLanguage<GenreList>("/genre/movie/list", language))

    /**
     * Get the list of official genres for TV shows
     *
     * @param language A language code
     */
    fun forTv(language: LocaleCode? = null) =
        httpExecutor.execute(getWithLanguage<GenreList>("/genre/tv/list", language))
}
