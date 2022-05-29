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
package io.v47.tmdb.http.tck

import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.tck.tests.*

class HttpClientTck {
    private val tests = listOf<TckTest>(
        ValidSimpleResponseTest(),
        ValidComplexResponseTest(),
        AuthErrorResponseTest(),
        NotFoundErrorResponseTest(),
        ValidImageResponseTest(),
        ImageNotFoundResponseTest(),
        InvalidImageSizeResponseTest()
    )

    fun verify(httpClientFactory: HttpClientFactory): TckResult {
        val failures = mutableListOf<TckResult.Failure.FailedTest>()
        tests.forEach { test ->
            val testResult = test.verify(httpClientFactory)
            if (testResult is TckTestResult.Failure)
                failures += TckResult.Failure.FailedTest(
                    test.javaClass.canonicalName,
                    testResult.expectedValue,
                    testResult.actualValue
                )
        }

        return if (failures.isEmpty())
            TckResult.Success
        else
            TckResult.Failure(failures)
    }
}

sealed class TckResult {
    object Success : TckResult()
    data class Failure(val failedTests: List<FailedTest>) : TckResult() {
        data class FailedTest(
            val name: String,
            val expectedValue: Any,
            val actualValue: Any?
        )
    }
}
