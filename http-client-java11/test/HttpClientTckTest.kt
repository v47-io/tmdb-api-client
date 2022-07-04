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
package io.v47.tmdb

import io.v47.tmdb.http.Java11HttpClientFactory
import io.v47.tmdb.http.tck.HttpClientTck
import io.v47.tmdb.http.tck.TckResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpClientTckTest {
    @Test
    fun executeTckTest() {
        Thread.sleep(5000) // To prevent being rate-limited

        val result = HttpClientTck().verify(Java11HttpClientFactory())

        if (result is TckResult.Failure)
            result.failedTests.forEach { failedTest ->
                Assertions.assertEquals(
                    failedTest.expectedValue,
                    failedTest.actualValue,
                    "Test ${failedTest.name} failed"
                )
            }
    }
}
