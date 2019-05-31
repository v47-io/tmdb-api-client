package io.v47.tmdb.http

import io.v47.tmdb.utils.TypeInfo
import org.reactivestreams.Publisher
import java.io.Closeable

interface HttpClientFactory {
    fun createHttpClient(baseUrl: String): HttpClient
}

interface HttpClient : Closeable {
    fun <T : Any> execute(request: HttpRequest, responseType: TypeInfo): Publisher<HttpResponse<T>>
}

interface HttpRequest {
    val method: HttpMethod
    val url: String
    val query: Map<String, List<Any>>
    val body: Any?
}

enum class HttpMethod {
    Get,
    Post,
    Put,
    Delete
}

interface HttpResponse<T> {
    val status: Int
    val headers: Map<String, List<String>>
    val body: T?
}
