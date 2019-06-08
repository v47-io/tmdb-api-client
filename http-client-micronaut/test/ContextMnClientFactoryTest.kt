package io.v47.tmdb.http

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContextMnClientFactoryTest {
    private lateinit var embeddedServer: EmbeddedServer

    @BeforeAll
    fun startApplicationContext() {
        embeddedServer = ApplicationContext.run(EmbeddedServer::class.java)

        while (!embeddedServer.isRunning) {
        }
    }

    @Test
    fun createFactoryTest() {
        val factory = ContextMnClientFactory(embeddedServer.applicationContext)
        assertDoesNotThrow {
            factory.createHttpClient("https://api.themoviedb.org")
        }
    }
}
