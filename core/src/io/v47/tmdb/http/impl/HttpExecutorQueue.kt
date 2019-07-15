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
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

internal class HttpExecutorQueue : CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.IO

    private val log = LoggerFactory.getLogger(javaClass)!!
    private val queue = Channel<QueuedRequest<*>>(Channel.UNLIMITED)

    @Suppress("ComplexMethod", "LongMethod", "ReturnCount", "ThrowsCount", "TooGenericExceptionCaught")
    fun start(httpClient: HttpClient) {
        launch {
            while (isActive) {
                val executingRequests = ConcurrentLinkedQueue<QueuedRequest<*>>()
                val rateLimited = AtomicBoolean(false)

                runCatching {
                    coroutineScope processingScope@{
                        @Suppress("UNCHECKED_CAST")
                        val c = queue as Channel<QueuedRequest<Any>>

                        for (nextRequest in c) {
                            executingRequests += nextRequest

                            launch requestWorker@{
                                if (rateLimited.get())
                                    return@requestWorker

                                val responsePublisher = httpClient.execute(
                                    nextRequest.httpRequest,
                                    nextRequest.responseType
                                )

                                val responseChannel = responsePublisher.openSubscription(1)

                                if (rateLimited.get()) {
                                    responseChannel.cancel()
                                    return@requestWorker
                                }

                                @Suppress("UNCHECKED_CAST")
                                val firstResponse = try {
                                    responseChannel.receive() as HttpResponse<Any>
                                } catch (x: Exception) {
                                    if (x !is CancellationException) {
                                        nextRequest.offerException(x)
                                        executingRequests -= nextRequest

                                        return@requestWorker
                                    }

                                    throw x
                                }

                                if (firstResponse.status == 429) {
                                    responseChannel.cancel()

                                    if (rateLimited.getAndSet(true))
                                        return@requestWorker

                                    val retryAfterHeader = firstResponse.headers["Retry-After"]?.firstOrNull()
                                    val retryAfterSeconds = (retryAfterHeader?.toInt() ?: 10)

                                    throw RateLimitException(retryAfterSeconds)
                                } else {
                                    val remainingHeader = firstResponse.headers["X-RateLimit-Remaining"]?.firstOrNull()
                                    val remaining = remainingHeader?.toInt() ?: 1

                                    executingRequests -= nextRequest

                                    launch(this@HttpExecutorQueue.coroutineContext) {
                                        runCatching {
                                            nextRequest.offerResponse(firstResponse)

                                            for (resp in responseChannel) {
                                                @Suppress("UNCHECKED_CAST")
                                                nextRequest.offerResponse(resp as HttpResponse<Any>)
                                            }
                                        }.onFailure {
                                            nextRequest.offerException(it)
                                        }

                                        nextRequest.close()
                                    }

                                    if (remaining == 0) {
                                        if (rateLimited.getAndSet(true))
                                            return@requestWorker

                                        val reset = firstResponse.headers["X-RateLimit-Reset"]?.firstOrNull()?.toInt()
                                        val delaySeconds = if (reset != null)
                                            (reset - (System.currentTimeMillis() / 1000)).toInt()
                                        else
                                            5

                                        if (delaySeconds > -1) {
                                            if (log.isTraceEnabled)
                                                log.trace("remaining limit is 0 and delay is {}", delaySeconds)

                                            throw RateLimitException(delaySeconds)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }.onFailure { x ->
                    if (x is RateLimitException) {
                        log.warn("Just got rate-limited, delaying further operations by {}s", x.delaySeconds)

                        delay(x.delaySeconds * 1000L)
                        executingRequests.forEach { req ->
                            if (!req.hasResponded)
                                queue.send(req)
                        }
                    } else
                        throw x
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
    private val _responseChannel = Channel<HttpResponse<T>>(Channel.UNLIMITED)
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

private class RateLimitException(val delaySeconds: Int) : RuntimeException()
