/**
 * Copyright 2020 The tmdb-api-v2 Authors
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

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.TypeInfo

internal class ValidImageResponseTest : AbstractTckTest("https://image.tmdb.org/t/p") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/{imageSize}/{imagePath}",
            mapOf(
                "imageSize" to "original",
                "imagePath" to "wwemzKWzjKYJFfCeiB57q3r4Bcm.svg"
            )
        )

        lateinit var response: HttpResponse<*>
        val contentLength = Flowable
            .fromPublisher(
                httpClient.execute(
                    request,
                    TypeInfo.Simple(ByteArray::class.java)
                )
            )
            .reduce(0) { acc, resp ->
                response = resp
                acc + (resp.body as ByteArray).size
            }
            .blockingGet()

        return when {
            response.status != 200 -> TckTestResult.Failure(200, response.status)
            contentLength != 2114 -> TckTestResult.Failure(2114, contentLength)
            else -> TckTestResult.Success
        }
    }
}
