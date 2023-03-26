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
import io.smallrye.mutiny.converters.multi.FromMono
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.TypeInfo
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.http.api.toErrorResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.concurrent.Flow

internal class HttpClientImpl(private val rawClient: WebClient) : HttpClient {
    private val om = ObjectMapper().apply {
        findAndRegisterModules()
    }

    private val imageErrorRegex = Regex("""<h\d>(.+?)</h\d>""", RegexOption.IGNORE_CASE)

    @Suppress("UNCHECKED_CAST")
    override fun execute(
        request: HttpRequest,
        responseType: TypeInfo
    ): Flow.Publisher<HttpResponse<out Any>> {
        val jsonBody = (responseType as? TypeInfo.Simple)?.type != ByteArray::class.java

        val requestSpec = request.toRequestSpec(jsonBody)

        return Multi
            .createFrom()
            .converter(
                FromMono.INSTANCE as FromMono<HttpResponse<out Any>>,
                requestSpec
                    .exchangeToMono { resp ->
                        if (resp.statusCode() == HttpStatus.OK) {
                            val typeReference =
                                ParameterizedTypeReference.forType<Any>(responseType.fullType)

                            resp.bodyToMono(typeReference).map { resp to it }
                        } else {
                            resp.bodyToMono(ByteArray::class.java)
                                .map { resp to readErrorBody(it, resp.statusCode().value()) }
                        }
                    }
                    .map { (resp, body) ->
                        HttpResponseImpl(
                            resp.statusCode().value(),
                            resp.headers().asHttpHeaders().toMap(),
                            body
                        )
                    }
                    .onErrorResume { t ->
                        if (t is HttpClientErrorException)
                            Mono.just(
                                HttpResponseImpl(
                                    t.statusCode.value(),
                                    t.responseHeaders?.toMap().orEmpty(),
                                    createErrorResponse(t)
                                )
                            )
                        else
                            throw IllegalArgumentException("Not a HttpClientErrorException", t)
                    } as Mono<HttpResponse<out Any>>
            )
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

        return readErrorBody(bodyByteArray, t.statusCode.value())
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
