package io.v47.tmdb.api

import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.Certification

class CertificationsApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get an up to date list of the officially supported movie certifications on TMDb
     */
    fun forMovies() =
        httpExecutor.execute(get<Certification>("/certification/movie/list"))

    /**
     * Get an up to date list of the officially supported TV show certifications on TMDb
     */
    fun forTv() =
        httpExecutor.execute(get<Certification>("/certification/tv/list"))
}
