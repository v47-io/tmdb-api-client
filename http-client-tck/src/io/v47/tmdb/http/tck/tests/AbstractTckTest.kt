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
package io.v47.tmdb.http.tck.tests

import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.tck.TckTest
import io.v47.tmdb.http.tck.TckTestResult

internal abstract class AbstractTckTest(private val baseUrl: String) : TckTest {
    protected val apiKey: String = (System.getProperty("tmdb.apiKey") ?: System.getenv("API_KEY")).let {
        if (it.isNullOrBlank())
            throw IllegalArgumentException(
                "Missing api key: You have to provide a valid TMDB API key " +
                        "that relates to a linked application. You can provide the key either as a system " +
                        "property called 'tmdb.apiKey' or as an environment variable called 'API_KEY'"
            )
        else
            it
    }

    override fun verify(httpClientFactory: HttpClientFactory): TckTestResult {
        val client = httpClientFactory.createHttpClient(baseUrl)
        return doVerify(client)
    }

    protected abstract fun doVerify(httpClient: HttpClient): TckTestResult
}
