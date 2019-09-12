package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.utils.TypeInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.time.delay
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

internal class HttpExecutorQueue(parentContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext = Job() + parentContext

    private val log = LoggerFactory.getLogger(javaClass)!!

    private val stateMutex = Mutex()
    private var requestsAvailable = 1

    private val requestQueue = Channel<QueuedRequest<*>>(Channel.UNLIMITED)

    fun start(httpClient: HttpClient) {
        launch {
            var isFirst = true

            while (isActive) {
                for (queuedRequest in requestQueue) {
                    runCatching {
                        var doReset = false

                        while (true) {
                            val doYield = stateMutex.withLock {
                                if (requestsAvailable > 0) {
                                    if (--requestsAvailable == 0)
                                        doReset = true

                                    false
                                } else
                                    true
                            }

                            if (doYield)
                                yield()
                            else
                                break
                        }

                        launch request@{
                            @Suppress("UNCHECKED_CAST")
                            val nextRequest = queuedRequest as QueuedRequest<Any>

                            val responsePublisher = httpClient.execute(
                                nextRequest.httpRequest,
                                nextRequest.responseType
                            )

                            @Suppress("EXPERIMENTAL_API_USAGE")
                            val responseChannel = responsePublisher.openSubscription(1)

                            @Suppress("UNCHECKED_CAST")
                            val firstResponse = try {
                                responseChannel.receive() as HttpResponse<Any>
                            } catch (x: Exception) {
                                stateMutex.withLock {
                                    requestsAvailable++
                                }

                                if (x !is CancellationException) {
                                    nextRequest.offerException(x)
                                    return@request
                                }

                                throw x
                            }

                            val isRateLimited = stateMutex.withLock {
                                val tmp = firstResponse.status == 429

                                if (tmp) {
                                    log.warn("Just got rate-limited by TMDB")
                                    requestsAvailable = 0
                                }

                                tmp
                            }

                            val (retryAfter: Instant?, newAvailableRequests: Int?) =
                                if (doReset || isRateLimited) {
                                    val headers = firstResponse.headers.toList()

                                    val xRatelimitLimit = headers
                                        .find { (name, _) -> name.equals("x-ratelimit-limit", true) }
                                        ?.second?.firstOrNull()?.toInt()

                                    val xRatelimitReset = headers
                                        .find { (name, _) -> name.equals("x-ratelimit-reset", true) }
                                        ?.second?.firstOrNull()?.toLong()

                                    if (isRateLimited)
                                        requestQueue.send(nextRequest)

                                    Instant.ofEpochSecond(
                                        (xRatelimitReset ?: System.currentTimeMillis() / 1000 + 10) + 1
                                    ) to (xRatelimitLimit ?: 40)
                                } else
                                    null to null

                            if (!isRateLimited) {
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
                            } else
                                responseChannel.cancel()

                            if (retryAfter != null && newAvailableRequests != null) {
                                if (!isFirst) {
                                    val delayDuration = Duration.between(Instant.now(), retryAfter)
                                    if (log.isDebugEnabled)
                                        log.debug(
                                            "Delaying further operations by {}ms after rate-limit",
                                            delayDuration.toMillis()
                                        )

                                    delay(delayDuration)
                                }

                                stateMutex.withLock {
                                    requestsAvailable = if (newAvailableRequests > 0)
                                        newAvailableRequests
                                    else
                                        1 // Prevents locking ourselves out
                                }
                            }

                            isFirst = false
                        }
                    }
                }
            }
        }
    }

    fun <T> enqueue(request: HttpRequest, responseType: TypeInfo): ReceiveChannel<HttpResponse<T>> {
        val queuedRequest = QueuedRequest<T>(request, responseType)

        runBlocking {
            if (!requestQueue.offer(queuedRequest))
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
