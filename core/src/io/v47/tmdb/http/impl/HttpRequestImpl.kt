package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpRequest

data class HttpRequestImpl(
    override val method: HttpMethod,
    override val url: String,
    override val uriVariables: Map<String, Any> = emptyMap(),
    override val query: Map<String, List<Any>> = emptyMap(),
    override val body: Any? = null
) : HttpRequest
