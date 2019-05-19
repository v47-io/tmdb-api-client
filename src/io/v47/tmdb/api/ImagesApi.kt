package io.v47.tmdb.api

import io.github.resilience4j.timelimiter.TimeLimiter
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.reactivex.Flowable
import io.v47.tmdb.TmdbClientException
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.model.*
import io.v47.tmdb.utils.TypeInfo
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

class ImagesApi internal constructor(
    private val httpClientFactory: HttpClientFactory,
    configuration: Configuration,
    timeLimiterConfig: TimeLimiterConfig
) {
    private val log = LoggerFactory.getLogger(javaClass)!!

    private val imageDlClient = (configuration.images.secureBaseUrl ?: configuration.images.baseUrl)
        ?.let { httpClientFactory.createHttpClient(it.trimEnd('/')) }
        ?: run {
            log.warn("Image download not possible: The system configuration doesn't provide a base URL!")
            null
        }

    private val timeLimiter = TimeLimiter.of(timeLimiterConfig)

    private val byteArrayTypeInfo = TypeInfo.Simple(ByteArray::class.java)
    private val imageSizeNotSupported = "Image size not supported"

    val available get() = imageDlClient != null

    @Suppress("ThrowsCount")
    fun download(imagePath: String, size: ImageSize = Original): Publisher<ByteArray> {
        if (!available)
            throw IllegalStateException(
                "Cannot download image: The system " +
                        "configuration doesn't provide a base URL!"
            )

        require(size is Width || size is Height || size is Original) { "Invalid size: $size" }

        val actualSize = if (imagePath.endsWith(".svg", ignoreCase = true))
            Original
        else
            size

        val request = HttpRequestImpl(HttpMethod.Get, "/$actualSize/$imagePath")

        return Flowable
            .fromPublisher(
                timeLimiter.executeFutureSupplier {
                    CompletableFuture.supplyAsync {
                        imageDlClient!!.execute<ByteArray>(
                            request,
                            byteArrayTypeInfo
                        )
                    }
                }
            )
            .doOnNext { resp ->
                @Suppress("MagicNumber")
                if (resp.status != 200) {
                    if (String(resp.body ?: ByteArray(0)).contains(imageSizeNotSupported, ignoreCase = true))
                        throw ErrorResponseException(
                            ErrorResponse(
                                imageSizeNotSupported,
                                resp.status
                            ),
                            request
                        )
                    else
                        throw TmdbClientException("The request failed with status ${resp.status}", request)
                }
            }
            .map { it.body ?: ByteArray(0) }
    }

    internal fun close() {
        imageDlClient?.close()
    }
}
