package io.v47.tmdb.autoconfigure

import io.v47.tmdb.TmdbClient
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [TmdbAutoConfiguration::class])
class TmdbAutoConfigurationTest {
    @Autowired
    private lateinit var tmdbClient: TmdbClient

    @Test
    fun `TMDB Client is available`() {
        assertNotNull(tmdbClient)
    }
}
