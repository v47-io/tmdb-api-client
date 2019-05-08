package io.v47.tmdb.api

import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.Credits

class CreditApi(private val httpExecutor: HttpExecutor) {
    /**
     * Get a movie or TV credit details by id
     *
     * @param creditId The credit id for a specific credit
     */
    fun details(creditId: String) =
        httpExecutor.execute(get<Credits>("/credit/$creditId"))
}
