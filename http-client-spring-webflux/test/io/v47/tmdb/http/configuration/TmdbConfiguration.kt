package io.v47.tmdb.http.configuration

import io.v47.tmdb.TmdbClient
import io.v47.tmdb.http.ContextWebClientFactory
import io.v47.tmdb.http.HttpClientFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean

@TestConfiguration
class TmdbConfiguration {
    @Value("#{environment.API_KEY}")
    private lateinit var apiKey: String

    @Bean
    fun contextWebClientFactory(applicationContext: ApplicationContext): HttpClientFactory =
        ContextWebClientFactory(applicationContext)

    @Bean
    fun tmdbClient(httpClientFactory: HttpClientFactory) =
        TmdbClient(httpClientFactory, apiKey)
}
