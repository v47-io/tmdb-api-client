package io.v47.tmdb.config

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.web.reactive.function.client.WebClient

@TestConfiguration
class TmdbAutoConfigurationTestConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()
}
