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

import io.reactivex.rxjava3.core.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo

internal class ValidComplexResponseTest : AbstractTckTest("https://api.themoviedb.org") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/3/company/{companyId}/alternative_names",
            mapOf("companyId" to 2),
            mapOf("api_key" to listOf(apiKey))
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                tmdbTypeReference<CompanyAlternativeNames>().toTypeInfo()
            )
        ).map { it.body!! }.blockingFirst()

        val checkValue = CompanyAlternativeNames(
            2,
            listOf(
                CompanyAlternativeNames.AlternativeName("Disney Pictures", ""),
                CompanyAlternativeNames.AlternativeName("월트 디즈니 픽처스", ""),
                CompanyAlternativeNames.AlternativeName("Disney", ""),
                CompanyAlternativeNames.AlternativeName("Walt Disney", ""),
            )
        )

        return if (result != checkValue)
            TckTestResult.Failure(checkValue, result)
        else
            TckTestResult.Success
    }

    data class CompanyAlternativeNames(
        val id: Int,
        val results: List<AlternativeName>
    ) {
        data class AlternativeName(val name: String, val type: String)
    }
}
