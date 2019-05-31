package io.v47.tmdb

import io.v47.tmdb.http.HttpRequest

open class TmdbClientException(
    message: String,
    val request: HttpRequest,
    cause: Throwable? = null
) : Exception(message, cause)
