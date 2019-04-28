package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpResponse

data class HttpResponseImpl(
        override val status: Int,
        override val headers: Map<String, List<String>> = emptyMap(),
        override val body: ByteArray? = null
) : HttpResponse {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpResponse) return false

        if (status != other.status) return false
        if (headers != other.headers) return false
        if (body != null) {
            if (other.body == null) return false
            if (!body.contentEquals(other.body!!)) return false
        } else if (other.body != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status
        result = 31 * result + headers.hashCode()
        result = 31 * result + (body?.contentHashCode() ?: 0)
        return result
    }
}
