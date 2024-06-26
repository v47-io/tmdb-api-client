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
package io.v47.tmdb.http.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.smallrye.mutiny.Multi
import io.v47.tmdb.http.*
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.http.api.toErrorResponse
import io.v47.tmdb.utils.ReadLibraryVersionUtil.readLibraryVersion
import java.net.URI
import java.net.URLEncoder
import java.util.concurrent.Flow
import java.net.http.HttpClient as JHttpClient
import java.net.http.HttpRequest as JHttpRequest
import java.net.http.HttpResponse as JHttpResponse

internal class Java11HttpClientImpl(
    private val objectMapper: ObjectMapper,
    private val baseUrl: String = ""
) : HttpClient {
    companion object {
        private const val OK = 200

        @JvmStatic
        private val VERSION = readLibraryVersion("http-client-java11")
    }

    private val uriVariableRegex = Regex("""\{(\w+)}""", RegexOption.IGNORE_CASE)
    private val imageErrorRegex = Regex("""<h\d>(.+?)</h\d>""", RegexOption.IGNORE_CASE)

    private val rawClient = JHttpClient
        .newBuilder()
        .followRedirects(JHttpClient.Redirect.NORMAL)
        .build()!!

    override fun execute(
        request: HttpRequest,
        responseType: TypeInfo
    ): Flow.Publisher<HttpResponse<out Any>> {
        val jsonBody = (responseType as? TypeInfo.Simple)?.type != ByteArray::class.java

        return Multi
            .createFrom()
            .completionStage(
                rawClient.sendAsync(
                    request.toJHttpRequest(jsonBody),
                    JHttpResponse.BodyHandlers.ofByteArray()
                )
            )
            .map { resp ->
                if (resp.statusCode() == OK)
                    resp.toHttpResponse(if (jsonBody) responseType else null)
                else
                    DefaultHttpResponse(
                        resp.statusCode(),
                        resp.headers().map(),
                        createErrorResponse(resp)
                    )
            }
    }

    private fun createErrorResponse(jResponse: JHttpResponse<ByteArray>) =
        runCatching {
            objectMapper.readValue(jResponse.body(), RawErrorResponse::class.java).toErrorResponse()
        }.getOrElse {
            val str = String(jResponse.body())
            val imageErrorMatch = imageErrorRegex.find(str)

            val msg = if (imageErrorMatch != null)
                imageErrorMatch.groupValues[1]
            else
                str

            ErrorResponse(msg, jResponse.statusCode())
        }

    private fun HttpRequest.toJHttpRequest(json: Boolean = true) =
        JHttpRequest.newBuilder(URI(createUri()))
            .apply {
                val actualBody = if (body is ByteArray)
                    body as ByteArray
                else if (body != null)
                    objectMapper.writeValueAsBytes(body)
                else
                    null

                when (method) {
                    HttpMethod.Get -> GET()
                    HttpMethod.Post -> POST(JHttpRequest.BodyPublishers.ofByteArray(actualBody))
                    HttpMethod.Put -> PUT(JHttpRequest.BodyPublishers.ofByteArray(actualBody))
                    HttpMethod.Delete ->
                        if (actualBody != null)
                            method("DELETE", JHttpRequest.BodyPublishers.ofByteArray(actualBody))
                        else
                            DELETE()
                }

                header(
                    "Accept", if (json)
                        "application/json"
                    else
                        "*/*"
                )

                header(
                    "Content-Type",
                    if (body !is ByteArray)
                        "application/json"
                    else
                        "application/octet-stream"
                )

                header(
                    "User-Agent",
                    "tmdb-api-client/$VERSION (http-client-java11)"
                )
            }.build()

    private fun HttpRequest.createUri(): String {
        val uriSB = StringBuilder(baseUrl)

        if (!url.startsWith("/"))
            uriSB.append('/')

        uriSB.append(url.replace(uriVariableRegex) { mr ->
            val name = mr.groupValues[1]
            uriVariables[name]?.toString()
                ?: throw IllegalArgumentException("No value specified for URI variable $name")
        })

        if (query.isNotEmpty()) {
            uriSB.append("?")

            var first = true
            query.map { (name, values) ->
                if (first)
                    first = false
                else
                    uriSB.append('&')

                uriSB.append(URLEncoder.encode(name, Charsets.UTF_8))
                uriSB.append('=')
                var valueFirst = true
                values.forEach { value ->
                    if (valueFirst)
                        valueFirst = false
                    else
                        uriSB.append(',')

                    uriSB.append(URLEncoder.encode(value.toString(), Charsets.UTF_8))
                }
            }
        }

        return uriSB.toString()
    }

    private fun JHttpResponse<ByteArray>.toHttpResponse(typeInfo: TypeInfo?): HttpResponse<out Any> =
        DefaultHttpResponse(
            statusCode(),
            headers().map(),
            if (typeInfo != null)
                parseBody(typeInfo)
            else
                body()
        )

    private fun JHttpResponse<ByteArray>.parseBody(typeInfo: TypeInfo) =
        objectMapper.readValue<Any>(
            body(),
            objectMapper.typeFactory.constructType(typeInfo.fullType)
        )
}
