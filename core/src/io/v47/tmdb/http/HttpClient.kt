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
package io.v47.tmdb.http

import org.reactivestreams.Publisher
import java.io.Closeable

interface HttpClientFactory {
    fun createHttpClient(baseUrl: String): HttpClient
}

interface HttpClient : Closeable {
    fun execute(
        request: HttpRequest,
        responseType: TypeInfo
    ): Publisher<HttpResponse<out Any>>
}

interface HttpRequest {
    val method: HttpMethod
    val url: String
    val uriVariables: Map<String, Any>
    val query: Map<String, List<Any>>
    val body: Any?
}

enum class HttpMethod {
    Get,
    Post,
    Put,
    Delete
}

interface HttpResponse<T> {
    val status: Int
    val headers: Map<String, List<String>>
    val body: T?
}
