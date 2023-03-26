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

import io.smallrye.mutiny.Multi
import io.v47.tmdb.api.key.TmdbApiKeyProvider
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.ErrorResponseException
import java.util.concurrent.Flow

private const val BASE_URL = "https://api.themoviedb.org"

@Suppress("MagicNumber")
internal class HttpExecutor(
    private val httpClientFactory: HttpClientFactory,
    private val apiKeyProvider: TmdbApiKeyProvider
) {
    private val httpClient: HttpClient by lazy {
        httpClientFactory.createHttpClient(BASE_URL)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> execute(request: TmdbRequest<T>): Flow.Publisher<T> {
        val httpRequest = request.toHttpRequest()

        return Multi
            .createFrom()
            .publisher(
                httpClient.execute(
                    httpRequest,
                    request.responseType
                )
            )
            .filter { it.body != null }
            .flatMap { resp ->
                when {
                    resp.status in 200..299 ->
                        Multi
                            .createFrom()
                            .item(
                                if (request.dropResponse)
                                    Unit as T
                                else
                                    resp.body as T
                            )

                    resp.body is ErrorResponse ->
                        Multi
                            .createFrom()
                            .failure(
                                ErrorResponseException(
                                    resp.body as ErrorResponse,
                                    httpRequest
                                )
                            )

                    else ->
                        Multi
                            .createFrom()
                            .failure(IllegalArgumentException("Invalid error response: $resp"))
                }
            }
    }

    private fun TmdbRequest<*>.toHttpRequest(): HttpRequest {
        val url = "/${apiVersion.value}/${path.trim(' ', '/')}"
        val query = queryArgs + ("api_key" to listOf(apiKeyProvider.getApiKey()))

        return DefaultHttpRequest(
            method,
            url,
            pathVariables,
            query,
            requestEntity
        )
    }
}
