package io.v47.tmdb.http.clientInjectTest

import io.v47.tmdb.TmdbClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SomeSingletonBean(
    @Inject
    val tmdbClient: TmdbClient
)
