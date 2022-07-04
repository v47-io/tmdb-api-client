/**
 * Copyright 2022 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.v47.tmdb.http.impl

import com.fasterxml.jackson.core.JacksonException
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

    private fun createErrorResponse(jResponse: JHttpResponse<ByteArray>): ErrorResponse {
        return try {
            objectMapper.readValue(jResponse.body(), RawErrorResponse::class.java).toErrorResponse()
        } catch (_: JacksonException) {
            val str = String(jResponse.body())
            val imageErrorMatch = imageErrorRegex.find(str)

            val msg = if (imageErrorMatch != null)
                imageErrorMatch.groupValues[1]
            else
                str

            ErrorResponse(msg, jResponse.statusCode())
        }
    }

    private fun HttpRequest.toJHttpRequest(json: Boolean = true): JHttpRequest =
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
