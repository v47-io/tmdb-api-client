package io.v47.tmdb.http

import io.v47.tmdb.http.configuration.TmdbConfiguration
import io.v47.tmdb.http.tck.HttpClientTck
import io.v47.tmdb.http.tck.TckResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ContextConfiguration(classes = [TmdbConfiguration::class])
class ContextWebClientTckTest {
    @Autowired
    private lateinit var httpClientFactory: HttpClientFactory

    @Test
    fun executeTckTest() {
        Thread.sleep(5000) // To prevent being rate-limited

        val result = HttpClientTck().verify(httpClientFactory)

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
