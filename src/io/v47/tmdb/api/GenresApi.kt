package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.GenreList

class GenresApi(private val httpExecutor: HttpExecutor) {
    /**
     * Get the list of official genres for movies
     *
     * @param language A language code
     */
    fun forMovies(language: LocaleCode? = null) =
        httpExecutor.execute(
            get<GenreList>("/genre/movie/list") {
                language?.let { queryArg("language", language.toString()) }
            }
        )

    /**
     * Get the list of official genres for TV shows
     *
     * @param language A language code
     */
    fun forTv(language: LocaleCode? = null) =
        httpExecutor.execute(
            get<GenreList>("/genre/tv/list") {
                language?.let { queryArg("language", language.toString()) }
            }
        )
}
