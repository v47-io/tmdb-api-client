package io.v47.tmdb.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.v47.tmdb.http.impl.HttpClientImpl
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient


class StandaloneWebClientFactory : HttpClientFactory {
    companion object {
        private val isTckRunning = runCatching {
            Class.forName("io.v47.tmdb.http.tck.HttpClientTck")
        }.isSuccess
    }

    private val connector = ReactorClientHttpConnector()

    private val objectMapper = ObjectMapper().apply {
        findAndRegisterModules()

        // Don't need to be so strict for TCK, it's testing the implementation of the client, not the deserialization
        if (isTckRunning)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private val exchangeStrategies = ExchangeStrategies
        .builder()
        .codecs { config ->
            config.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
            config.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
        }
        .build()

    override fun createHttpClient(baseUrl: String): HttpClient {
        return HttpClientImpl(
            WebClient.builder()
                .clientConnector(connector)
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(baseUrl)
                .build()
        )
    }
}
