/**
 * Copyright 2022 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
