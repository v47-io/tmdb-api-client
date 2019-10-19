package io.v47.tmdb.autoconfigure

import io.v47.tmdb.TmdbClient
import io.v47.tmdb.http.ContextWebClientFactory
import io.v47.tmdb.http.HttpClientFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration

@Configuration
@Conditional(TmdbAutoConfigurationCondition::class)
class TmdbAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(HttpClientFactory::class)
    fun contextWebClientFactory(applicationContext: ApplicationContext): HttpClientFactory =
        ContextWebClientFactory(applicationContext)

    @Bean
    @ConditionalOnMissingBean(TmdbClient::class)
    fun tmdbClient(
        httpClientFactory: HttpClientFactory,
        @Value("\${tmdb-client.api-key:#{systemEnvironment['TMDB_API_KEY']}}")
        apiKey: String
    ) =
        TmdbClient(httpClientFactory, apiKey)
}
