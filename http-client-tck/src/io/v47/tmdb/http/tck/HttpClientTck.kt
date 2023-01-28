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
package io.v47.tmdb.http.tck

import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.tck.tests.AuthErrorResponseTest
import io.v47.tmdb.http.tck.tests.ImageNotFoundResponseTest
import io.v47.tmdb.http.tck.tests.InvalidImageSizeResponseTest
import io.v47.tmdb.http.tck.tests.NotFoundErrorResponseTest
import io.v47.tmdb.http.tck.tests.ValidComplexResponseTest
import io.v47.tmdb.http.tck.tests.ValidImageResponseTest
import io.v47.tmdb.http.tck.tests.ValidSimpleResponseTest
import java.io.Serializable

/**
 * The TMDb API HttpClient TCK runner.
 *
 * Just call [verify] to ensure your [HttpClient][io.v47.tmdb.http.HttpClient] implementation
 * is compatible.
 */
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

@Suppress("SerialVersionUIDInSerializableClass")
sealed class TckResult : Serializable {
    object Success : TckResult(), Serializable
    data class Failure(val failedTests: List<FailedTest>) : TckResult(), Serializable {
        data class FailedTest(
            val name: String,
            val expectedValue: Any,
            val actualValue: Any?
        ) : Serializable
    }
}
