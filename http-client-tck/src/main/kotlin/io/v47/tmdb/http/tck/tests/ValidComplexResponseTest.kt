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
package io.v47.tmdb.http.tck.tests

import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.impl.DefaultHttpRequest
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.http.tck.utils.blockingFirst
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo
import java.io.Serializable

internal class ValidComplexResponseTest : AbstractTckTest("https://api.themoviedb.org") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val request = DefaultHttpRequest(
            HttpMethod.Get,
            "/3/company/{companyId}/alternative_names",
            mapOf("companyId" to 2),
            mapOf("api_key" to listOf(apiKey))
        )

        val result =
            httpClient.execute(
                request,
                tmdbTypeReference<CompanyAlternativeNames>().toTypeInfo()
            ).blockingFirst().body

        val checkValue = CompanyAlternativeNames(
            2,
            listOf(
                CompanyAlternativeNames.AlternativeName("Disney Pictures", ""),
                CompanyAlternativeNames.AlternativeName("월트 디즈니 픽처스", ""),
                CompanyAlternativeNames.AlternativeName("Disney", ""),
                CompanyAlternativeNames.AlternativeName("Walt Disney", ""),
                CompanyAlternativeNames.AlternativeName("Walt Disney Productions", ""),
                CompanyAlternativeNames.AlternativeName("华特迪士尼公司", ""),
                CompanyAlternativeNames.AlternativeName("迪士尼影片公司", "")
            )
        )

        return if (result != checkValue)
            TckTestResult.Failure(checkValue, result)
        else
            TckTestResult.Success
    }

    @Suppress("SerialVersionUIDInSerializableClass")
    data class CompanyAlternativeNames(
        val id: Int,
        val results: List<AlternativeName>
    ) : Serializable {
        data class AlternativeName(val name: String, val type: String) : Serializable
    }
}
