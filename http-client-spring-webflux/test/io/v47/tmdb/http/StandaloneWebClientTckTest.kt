package io.v47.tmdb.http

import io.v47.tmdb.http.tck.HttpClientTck
import io.v47.tmdb.http.tck.TckResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StandaloneWebClientTckTest {
    @Test
    fun executeTckText() {
        val result = HttpClientTck().verify(StandaloneWebClientFactory())

        if (result is TckResult.Failure)
            result.failedTests.forEach { failedTest ->
                assertEquals(
                    failedTest.expectedValue,
                    failedTest.actualValue,
                    "Test ${failedTest.name} failed"
                )
            }
    }
}
