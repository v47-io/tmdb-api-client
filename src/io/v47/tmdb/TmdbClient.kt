package io.v47.tmdb

import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.impl.HttpExecutor

class TmdbClient(
    httpClientFactory: HttpClientFactory,
    private val apiKey: String
) {
    private val httpExecutor = HttpExecutor(httpClientFactory, apiKey)
}
