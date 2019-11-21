package io.v47.tmdb.api

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.ErrorResponseException
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.model.*
import io.v47.tmdb.utils.TypeInfo
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

class ImagesApi internal constructor(
    private val httpClientFactory: HttpClientFactory,
    configuration: Configuration
) {
    private val log = LoggerFactory.getLogger(javaClass)!!

    private val imageDlClient = (configuration.images.secureBaseUrl ?: configuration.images.baseUrl)
        ?.let { httpClientFactory.createHttpClient(it.trimEnd('/')) }
        ?: run {
            log.warn("Image download not possible: The system configuration doesn't provide a base URL!")
            null
        }

    private val byteArrayTypeInfo = TypeInfo.Simple(ByteArray::class.java)

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

        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/{actualSize}/{imagePath}",
            mapOf(
                "actualSize" to actualSize,
                "imagePath" to imagePath
            )
        )

        return Flowable
            .fromPublisher(
                imageDlClient!!.execute(
                    request,
                    byteArrayTypeInfo
                )
            )
            .subscribeOn(Schedulers.io())
            .map { resp ->
                @Suppress("MagicNumber")
                when {
                    resp.status == 200 -> resp.body as ByteArray
                    resp.body is ErrorResponse -> throw ErrorResponseException(
                        resp.body as ErrorResponse,
                        request
                    )
                    else -> throw IllegalArgumentException("Invalid error response: $resp")
                }
            }
    }

    internal fun close() {
        imageDlClient?.close()
    }
}
