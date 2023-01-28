/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.api

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.TypeInfo
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.ErrorResponseException
import io.v47.tmdb.http.impl.DefaultHttpRequest
import io.v47.tmdb.model.Configuration
import io.v47.tmdb.model.Height
import io.v47.tmdb.model.ImageSize
import io.v47.tmdb.model.Original
import io.v47.tmdb.model.Width
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

        val request = DefaultHttpRequest(
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
