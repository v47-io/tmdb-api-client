package io.v47.tmdb.http.impl

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.ErrorResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.reactive.asPublisher
import org.reactivestreams.Publisher
import java.util.concurrent.atomic.AtomicBoolean

private const val BASE_URL = "https://api.themoviedb.org"

@Suppress("MagicNumber")
class HttpExecutor(
    private val httpClientFactory: HttpClientFactory,
    private val apiKey: String
) {
    private lateinit var httpClient: HttpClient
    private var queue = HttpExecutorQueue(Dispatchers.IO)
    private var httpClientInitialized = AtomicBoolean(false)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> execute(request: TmdbRequest<T>): Publisher<T> {
        val httpRequest = createHttpRequest(request)

        if (!httpClientInitialized.getAndSet(true)) {
            httpClient = httpClientFactory.createHttpClient(BASE_URL)
            queue.start(httpClient)
        }

        @Suppress("EXPERIMENTAL_API_USAGE")
        return Flowable
            .fromPublisher(queue.enqueue<T>(httpRequest, request.responseType).consumeAsFlow().asPublisher())
            .filter { it.body != null }
            .map { resp ->
                when {
                    resp.status == 200 -> resp.body
                    resp.body is ErrorResponse -> throw ErrorResponseException(
                        resp.body as ErrorResponse,
                        httpRequest
                    )
                    else -> throw IllegalArgumentException("Invalid error response: $resp")
                }
            }
    }

    private fun createHttpRequest(tmdbRequest: TmdbRequest<*>): HttpRequest {
        val url = "/${tmdbRequest.apiVersion}/${tmdbRequest.path.trim(' ', '/')}"
        val query = tmdbRequest.queryArgs + ("api_key" to listOf(apiKey))

        return HttpRequestImpl(tmdbRequest.method, url, query, tmdbRequest.requestEntity)
    }
}
