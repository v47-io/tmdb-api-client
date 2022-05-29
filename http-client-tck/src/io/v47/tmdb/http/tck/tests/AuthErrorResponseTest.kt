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
package io.v47.tmdb.http.tck.tests

import io.reactivex.rxjava3.core.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo

@Suppress("MagicNumber")
internal class AuthErrorResponseTest : AbstractTckTest("https://api.themoviedb.org") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val checkResponse = ErrorResponse("Invalid API key: You must be granted a valid key.", 7)

        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/3/company/{companyId}",
            mapOf("companyId" to 2)
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                tmdbTypeReference<ValidSimpleResponseTest.Company>().toTypeInfo()
            )
        ).blockingFirst()

        return if (result.status != 401)
            TckTestResult.Failure(401, result.status)
        else {
            @Suppress("CAST_NEVER_SUCCEEDS")
            val errorResponse = result.body as? ErrorResponse
            if (errorResponse !is ErrorResponse || errorResponse != checkResponse)
                TckTestResult.Failure(checkResponse, errorResponse)
            else
                TckTestResult.Success
        }
    }
}
