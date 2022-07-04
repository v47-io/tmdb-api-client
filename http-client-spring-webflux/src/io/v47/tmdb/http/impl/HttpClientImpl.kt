/**
 * Copyright 2022 The tmdb-api-client Authors
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

import com.fasterxml.jackson.databind.ObjectMapper
import io.v47.tmdb.http.*
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.http.api.toErrorResponse
import org.reactivestreams.Publisher
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

internal class HttpClientImpl(private val rawClient: WebClient) : HttpClient {
    private val om = ObjectMapper().apply {
        findAndRegisterModules()
    }

    private val imageErrorRegex = Regex("""<h\d>(.+?)</h\d>""", RegexOption.IGNORE_CASE)

    @Suppress("UNCHECKED_CAST")
    override fun execute(
        request: HttpRequest,
        responseType: TypeInfo
    ): Publisher<HttpResponse<out Any>> {
        val jsonBody = (responseType as? TypeInfo.Simple)?.type != ByteArray::class.java

        val requestSpec = request.toRequestSpec(jsonBody)

        return requestSpec
            .exchangeToMono { resp ->
                if (resp.statusCode() == HttpStatus.OK) {
                    val typeReference =
                        ParameterizedTypeReference.forType<Any>(responseType.fullType)

                    resp.bodyToMono(typeReference).map { resp to it }
                } else {
                    resp.bodyToMono(ByteArray::class.java)
                        .map { resp to readErrorBody(it, resp.rawStatusCode()) }
                }
            }
            .map { (resp, body) ->
                HttpResponseImpl(
                    resp.rawStatusCode(),
                    resp.headers().asHttpHeaders().toMap(),
                    body
                )
            }
            .onErrorResume { t ->
                if (t is HttpClientErrorException)
                    Mono.just(
                        HttpResponseImpl(
                            t.rawStatusCode,
                            t.responseHeaders?.toMap() ?: emptyMap(),
                            createErrorResponse(t)
                        )
                    )
                else
                    throw IllegalArgumentException("Not a HttpClientErrorException", t)
            } as Publisher<HttpResponse<out Any>>
    }

    @Suppress("ComplexMethod")
    private fun HttpRequest.toRequestSpec(jsonBody: Boolean): WebClient.RequestHeadersSpec<*> {
        val reqSpec = when (method) {
            HttpMethod.Get -> rawClient.get()
            HttpMethod.Post -> rawClient.post()
            HttpMethod.Put -> rawClient.put()
            HttpMethod.Delete -> rawClient.delete()
        }

        reqSpec.uri { uriBuilder ->
            uriBuilder.path(url)

            query.forEach { (name, values) ->
                uriBuilder.queryParam(name, values.joinToString(","))
            }

            uriBuilder.build(uriVariables)
        }
            .accept(
                if (jsonBody)
                    MediaType.APPLICATION_JSON
                else
                    MediaType.ALL
            )

        val body = body

        val optBodyReqSpec = if (reqSpec is WebClient.RequestBodyUriSpec && body != null)
            reqSpec.body(BodyInserters.fromValue(body))
        else
            reqSpec

        return optBodyReqSpec
            .header(
                HttpHeaders.CONTENT_TYPE,
                if (body !is ByteArray)
                    MediaType.APPLICATION_JSON_VALUE
                else
                    MediaType.APPLICATION_OCTET_STREAM_VALUE
            )
    }

    private fun createErrorResponse(t: HttpClientErrorException): ErrorResponse {
        val bodyByteArray = t.responseBodyAsByteArray

        return readErrorBody(bodyByteArray, t.rawStatusCode)
    }

    private fun readErrorBody(bodyByteArray: ByteArray, status: Int) =
        runCatching { om.readValue(bodyByteArray, RawErrorResponse::class.java) }
            .map { it.toErrorResponse() }
            .getOrElse {
                val txt = String(bodyByteArray, Charsets.UTF_8).let { str ->
                    val imageErrorMatch = imageErrorRegex.find(str)
                    if (imageErrorMatch != null)
                        imageErrorMatch.groupValues[1]
                    else
                        str
                }

                ErrorResponse(txt, status)
            }

    override fun close() = Unit
}
