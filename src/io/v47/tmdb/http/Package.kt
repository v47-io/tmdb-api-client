package io.v47.tmdb.http

import org.reactivestreams.Publisher

interface HttpClient {
    fun execute(request: HttpRequest): Publisher<HttpResponse>
}

interface HttpRequest {
    val method: HttpMethod
    val url: String
    val query: Map<String, String>
    val headers: Map<String, List<String>>
    val body: ByteArray?
}

enum class HttpMethod {
    Get,
    Post
}

interface HttpResponse {
    val status: Int
    val headers: Map<String, List<String>>
    val body: ByteArray?
}
