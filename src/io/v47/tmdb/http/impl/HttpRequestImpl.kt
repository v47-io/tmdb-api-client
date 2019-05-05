package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpRequest

internal data class HttpRequestImpl(
    override val method: HttpMethod,
    override val url: String,
    override val query: Map<String, List<Any>> = emptyMap(),
    override val body: Any? = null
) : HttpRequest
