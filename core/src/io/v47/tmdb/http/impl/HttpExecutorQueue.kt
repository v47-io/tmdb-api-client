package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.utils.TypeInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

internal class HttpExecutorQueue : CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.IO

    private val log = LoggerFactory.getLogger(javaClass)!!
    private val queue = Channel<QueuedRequest<*>>(Channel.UNLIMITED)

    fun start(httpClient: HttpClient) {
        val coreContext = coroutineContext

        launch {
            while (isActive) {
                val executingRequests = mutableListOf<QueuedRequest<*>>()
                var retryAfterSeconds: Int? = null

                processing@ while (isActive) {
                    @Suppress("UNCHECKED_CAST")
                    val nextRequest = queue.receive() as QueuedRequest<Any>

                    executingRequests += nextRequest

                    val responsePublisher = httpClient.execute(nextRequest.httpRequest, nextRequest.responseType)
                    val responseChannel = responsePublisher.openSubscription(1)

                    @Suppress("UNCHECKED_CAST")
                    val firstResponse = try {
                        responseChannel.receive() as HttpResponse<Any>
                    } catch (x: Exception) {
                        nextRequest.offerException(x)
                        executingRequests -= nextRequest
                        continue@processing
                    }

                    if (firstResponse.status == 429) {
                        val retryAfterHeader = firstResponse.headers["Retry-After"]?.firstOrNull()
                        retryAfterSeconds = min(1, retryAfterHeader?.toInt() ?: 10)

                        break@processing
                    }

                    executingRequests -= nextRequest

                    launch(coreContext) {
                        nextRequest.offerResponse(firstResponse)

                        runCatching {
                            for (resp in responseChannel) {
                                @Suppress("UNCHECKED_CAST")
                                nextRequest.offerResponse(resp as HttpResponse<Any>)
                            }

                            nextRequest.close()
                        }.onFailure {
                            nextRequest.offerException(it)
                        }
                    }
                }

                if (retryAfterSeconds != null) {
                    log.warn("Just got rate-limited, delaying further operations by {}s", retryAfterSeconds)

                    delay(retryAfterSeconds * 1000L)
                    executingRequests.forEach { req ->
                        if (!req.hasResponded)
                            queue.send(req)
                    }
                }
            }

            if (log.isDebugEnabled)
                log.debug("HttpExecutorQueue processing coroutine is ending")
        }
    }

    fun <T> enqueue(request: HttpRequest, responseType: TypeInfo): ReceiveChannel<HttpResponse<T>> {
        val queuedRequest = QueuedRequest<T>(request, responseType)

        runBlocking {
            if (!queue.offer(queuedRequest))
                throw IllegalArgumentException("The queue rejected the request $request -> $responseType")
        }

        return queuedRequest.responseChannel
    }
}

private class QueuedRequest<T>(val httpRequest: HttpRequest, val responseType: TypeInfo) {
    private val _responseChannel = Channel<HttpResponse<T>>()
    val responseChannel: ReceiveChannel<HttpResponse<T>> get() = _responseChannel

    private var _hasResponded = AtomicBoolean(false)
    val hasResponded get() = _hasResponded.get()

    suspend fun offerResponse(response: HttpResponse<T>) {
        _hasResponded.set(true)
        _responseChannel.send(response)
    }

    fun offerException(x: Throwable) {
        _hasResponded.set(true)
        _responseChannel.close(x)
    }

    fun close() {
        _responseChannel.close()
    }
}
