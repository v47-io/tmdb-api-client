/**
 * The Clear BSD License
 *
 * Copyright (c) 2025, the tmdb-api-client authors
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
package io.v47.tmdb.quarkus.impl

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpResponseStatus
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod.Delete
import io.v47.tmdb.http.HttpMethod.Get
import io.v47.tmdb.http.HttpMethod.Post
import io.v47.tmdb.http.HttpMethod.Put
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.TypeInfo
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.http.api.toErrorResponse
import io.v47.tmdb.http.impl.DefaultHttpResponse
import io.vertx.core.http.RequestOptions
import io.vertx.mutiny.core.MultiMap
import io.vertx.mutiny.core.buffer.Buffer
import io.vertx.mutiny.ext.web.client.WebClient
import io.vertx.mutiny.ext.web.codec.BodyCodec
import java.net.URI
import java.net.URLEncoder
import java.util.concurrent.Flow
import io.vertx.core.http.HttpMethod as VxHttpMethod
import io.vertx.mutiny.ext.web.client.HttpResponse as VxHttpResponse

private val URI_VARIABLE_PATTERN = Regex("""\{(\w+)}""", RegexOption.IGNORE_CASE)
private val IMAGE_ERROR_PATTERN = Regex("""<h\d>(.+?)</h\d>""", RegexOption.IGNORE_CASE)

internal class QuarkusHttpClientImpl(
    private val baseUrl: String,
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper
) : HttpClient {
    override fun execute(
        request: HttpRequest,
        responseType: TypeInfo
    ): Flow.Publisher<HttpResponse<out Any>> {
        val jsonBody = responseType.isNotByteArray

        val vxRequest = request.mapHttpRequest(jsonBody)
        vxRequest.`as`(BodyCodec.buffer())

        val body =
            if (request.body is ByteArray)
                Buffer.buffer(request.body as ByteArray)
            else
                request.body?.let { Buffer.buffer(objectMapper.writeValueAsBytes(it)) }

        val vxResponse =
            if (body != null)
                vxRequest.sendBuffer(body)
            else
                vxRequest.send()

        return vxResponse
            .toMulti()
            .map { bufferHttpResponse ->
                if (bufferHttpResponse.statusCode() == HttpResponseStatus.OK.code())
                    bufferHttpResponse.mapHttpResponse(if (jsonBody) responseType else null)
                else
                    DefaultHttpResponse(
                        bufferHttpResponse.statusCode(),
                        bufferHttpResponse.headers().toMap(),
                        bufferHttpResponse.createErrorResponse()
                    )
            }
    }


    private fun HttpRequest.mapHttpRequest(json: Boolean) =
        webClient.request(
            mapHttpMethod(),
            RequestOptions().apply {
                setAbsoluteURI(createUri())
                addHeader("Accept", if (json) "application/json" else "*/*")
                addHeader(
                    "Content-Type",
                    if (body is ByteArray) "application/octet-stream" else "application/json"
                )
            }
        )

    private fun HttpRequest.createUri() =
        buildString {
            append(baseUrl)

            if (!url.startsWith('/'))
                append('/')

            append(
                URI_VARIABLE_PATTERN.replace(url) { mr ->
                    val name = mr.groupValues[1]
                    val value = uriVariables[name]
                        ?: throw IllegalArgumentException("No value specified for URI variable $name")

                    "$value"
                }
            )

            if (!query.isEmpty()) {
                append('?')

                var first = true
                query.entries.forEach { (name, values) ->
                    if (first)
                        first = false
                    else
                        append('&')

                    append(URLEncoder.encode(name, Charsets.UTF_8))
                    append('=')

                    var valueFirst = true
                    values.forEach { value ->
                        if (valueFirst)
                            valueFirst = false
                        else
                            append(',')

                        append(URLEncoder.encode("$value", Charsets.UTF_8))
                    }
                }
            }
        }.let {
            URI.create(it).toURL()
        }

    private fun VxHttpResponse<Buffer>.mapHttpResponse(typeInfo: TypeInfo?) =
        DefaultHttpResponse(
            statusCode(),
            headers().toMap(),
            readResponseBody(typeInfo)
        )

    private fun VxHttpResponse<Buffer>.readResponseBody(typeInfo: TypeInfo?) =
        if (typeInfo != null)
            objectMapper.readValue(body().bytes, object : TypeReference<Any>() {
                override fun getType() = typeInfo.fullType
            })
        else
            body().bytes

    private fun VxHttpResponse<Buffer>.createErrorResponse() =
        try {
            objectMapper
                .readValue(body().bytes, RawErrorResponse::class.java)
                .toErrorResponse()
        } catch (_: JsonProcessingException) {
            val str = bodyAsString()

            ErrorResponse(
                IMAGE_ERROR_PATTERN
                    .find(str)
                    ?.let { it.groupValues[1] }
                    ?: str,
                statusCode()
            )
        }
}

private val TypeInfo.isNotByteArray
    get() =
        when (this) {
            is TypeInfo.Simple -> type != ByteArray::class.java
            else -> true
        }

private fun HttpRequest.mapHttpMethod() =
    when (method) {
        Get -> VxHttpMethod.GET
        Post -> VxHttpMethod.POST
        Put -> VxHttpMethod.PUT
        Delete -> VxHttpMethod.DELETE
    }

private fun MultiMap.toMap() =
    groupBy(
        { (key, _) -> key },
        { (_, value) -> value }
    )
