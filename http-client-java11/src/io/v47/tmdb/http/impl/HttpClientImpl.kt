/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.http.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.rxjava3.core.Flowable
import io.v47.tmdb.http.*
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.http.api.toErrorResponse
import org.reactivestreams.Publisher
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient as JHttpClient
import java.net.http.HttpRequest as JHttpRequest
import java.net.http.HttpResponse as JHttpResponse

internal class HttpClientImpl(
    private val objectMapper: ObjectMapper,
    private val baseUrl: String = ""
) : HttpClient {
    companion object {
        private const val OK = 200
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
    ): Publisher<HttpResponse<out Any>> {
        val jsonBody = (responseType as? TypeInfo.Simple)?.type != ByteArray::class.java

        return Flowable.fromFuture(
            rawClient.sendAsync(
                request.toJHttpRequest(jsonBody),
                JHttpResponse.BodyHandlers.ofByteArray()
            )
        ).map { resp ->
            if (resp.statusCode() == OK)
                resp.toHttpResponse(if (jsonBody) responseType else null)
            else
                HttpResponseImpl(
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
                    HttpMethod.Delete -> DELETE()
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

    private fun JHttpResponse<ByteArray>.toHttpResponse(typeInfo: TypeInfo?) =
        HttpResponseImpl(
            statusCode(),
            headers().map(),
            if (typeInfo != null)
                parseBody(typeInfo)
            else
                body()
        )

    private fun JHttpResponse<ByteArray>.parseBody(typeInfo: TypeInfo) =
        objectMapper.readValue(body(), typeInfo.fullType as Class<*>)

    override fun close() = Unit
}