package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpResponse

internal data class HttpResponseImpl<T : Any>(
    override val status: Int,
    override val headers: Map<String, List<String>> = emptyMap(),
    override val body: T? = null
) : HttpResponse<T>
