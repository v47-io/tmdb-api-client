package io.v47.tmdb.http.clientInjectTest

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.v47.tmdb.TmdbClient
import io.v47.tmdb.http.ContextMnClientFactory
import javax.inject.Inject
import javax.inject.Singleton

@Factory
class SomeFactory {
    @Bean
    @Singleton
    @Inject
    fun tmdbClient(
        applicationContext: ApplicationContext,
        @Value("\${API_KEY}")
        apiKey: String
    ) =
        TmdbClient(
            ContextMnClientFactory(applicationContext),
            apiKey
        )
}
