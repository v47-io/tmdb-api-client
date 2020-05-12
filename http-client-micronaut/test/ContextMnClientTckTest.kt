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
package io.v47.tmdb.http

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.v47.tmdb.http.tck.HttpClientTck
import io.v47.tmdb.http.tck.TckResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContextMnClientTckTest {
    private lateinit var embeddedServer: EmbeddedServer

    @BeforeAll
    fun startApplicationContext() {
        embeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    }

    @Test
    fun executeTckTest() {
        Thread.sleep(5000) // To prevent being rate-limited

        val result = HttpClientTck().verify(ContextMnClientFactory(embeddedServer.applicationContext))

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
