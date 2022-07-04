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
import io.v47.tmdb.http.TypeInfo
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult

@Suppress("MagicNumber")
internal class ImageNotFoundResponseTest : AbstractTckTest("https://image.tmdb.org/t/p") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val checkError = ErrorResponse("File Not Found", 404)

        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/{imageSize}/{imageFile}",
            mapOf(
                "imageSize" to "original",
                "imageFile" to "wwemzKWzjKYJ3r4cm.svg"
            )
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                TypeInfo.Simple(ByteArray::class.java)
            )
        ).blockingFirst()

        return if (result.status != 404)
            TckTestResult.Failure(404, result.status)
        else if (result.body !is ErrorResponse || result.body != checkError)
            TckTestResult.Failure(checkError, result.body)
        else
            TckTestResult.Success
    }
}
