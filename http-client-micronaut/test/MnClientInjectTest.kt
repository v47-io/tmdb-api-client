package io.v47.tmdb.http

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.v47.tmdb.http.clientInjectTest.SomeSingletonBean
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MnClientInjectTest {
    private lateinit var embeddedServer: EmbeddedServer

    @BeforeAll
    fun startServer() {
        embeddedServer = ApplicationContext.run(EmbeddedServer::class.java)

        while (!embeddedServer.isRunning) {
            Thread.onSpinWait()
        }
    }

    @Test
    fun testGetTmdbClientBean() {
        embeddedServer.applicationContext.getBean(SomeSingletonBean::class.java)
    }
}
