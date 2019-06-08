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

        while (!embeddedServer.isRunning) {
        }
    }

    @Test
    fun executeTckTest() {
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
