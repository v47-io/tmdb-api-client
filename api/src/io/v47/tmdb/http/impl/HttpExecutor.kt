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

import io.reactivex.rxjava3.core.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.ErrorResponseException
import org.reactivestreams.Publisher

private const val BASE_URL = "https://api.themoviedb.org"

@Suppress("MagicNumber")
internal class HttpExecutor(
    private val httpClientFactory: HttpClientFactory,
    private val apiKey: String
) {
    private val httpClient: HttpClient by lazy {
        httpClientFactory.createHttpClient(BASE_URL)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> execute(request: TmdbRequest<T>): Publisher<T> {
        val httpRequest = request.toHttpRequest()

        return Flowable
            .fromPublisher(
                httpClient.execute(
                    httpRequest,
                    request.responseType
                )
            )
            .filter { it.body != null }
            .map { resp ->
                when {
                    resp.status == 200 -> resp.body as T
                    resp.body is ErrorResponse -> throw ErrorResponseException(
                        resp.body as ErrorResponse,
                        httpRequest
                    )

                    else -> throw IllegalArgumentException("Invalid error response: $resp")
                }
            }
    }

    private fun TmdbRequest<*>.toHttpRequest(): HttpRequest {
        val url = "/${apiVersion.value}/${path.trim(' ', '/')}"
        val query = queryArgs + ("api_key" to listOf(apiKey))

        return DefaultHttpRequest(
            method,
            url,
            pathVariables,
            query,
            requestEntity
        )
    }
}
