package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpRequest

internal data class HttpRequestImpl(
    override val method: HttpMethod,
    override val url: String,
    override val headers: Map<String, List<String>> = emptyMap(),
    override val query: Map<String, String> = emptyMap(),
    override val body: ByteArray? = null
) : HttpRequest {
    @Suppress("ComplexMethod")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpRequest) return false

        if (method != other.method) return false
        if (url != other.url) return false
        if (headers != other.headers) return false
        if (query != other.query) return false
        if (body != null) {
            if (other.body == null) return false
            if (!body.contentEquals(other.body!!)) return false
        } else if (other.body != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = method.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + query.hashCode()
        result = 31 * result + (body?.contentHashCode() ?: 0)
        return result
    }
}
