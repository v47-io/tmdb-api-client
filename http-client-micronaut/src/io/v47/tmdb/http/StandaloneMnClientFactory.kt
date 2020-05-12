/**
 * Copyright 2020 The tmdb-api-v2 Authors
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
import io.micronaut.core.io.ResourceResolver
import io.micronaut.http.client.DefaultHttpClientConfiguration
import io.micronaut.http.client.loadbalance.FixedLoadBalancer
import io.micronaut.http.client.netty.DefaultHttpClient
import io.micronaut.http.client.netty.ssl.NettyClientSslBuilder
import io.micronaut.http.codec.MediaTypeCodecRegistry
import io.micronaut.jackson.codec.JsonMediaTypeCodec
import io.micronaut.jackson.codec.JsonStreamMediaTypeCodec
import io.micronaut.runtime.ApplicationConfiguration
import io.v47.tmdb.http.impl.HttpClientImpl
import io.v47.tmdb.http.utils.getBasePath
import java.net.URL
import java.time.Duration

class StandaloneMnClientFactory : HttpClientFactory {
    companion object {
        private val isTckRunning = runCatching {
            Class.forName("io.v47.tmdb.http.tck.HttpClientTck")
        }.isSuccess
    }

    private val httpClientConfiguration = DefaultHttpClientConfiguration().apply {
        @Suppress("MagicNumber")
        setReadTimeout(Duration.ofSeconds(30))
    }

    private val sslFactory = NettyClientSslBuilder(ResourceResolver())

    private val objectMapper = ObjectMapper().apply {
        findAndRegisterModules()

        // Don't need to be so strict for TCK, it's testing the implementation of the client, not the deserialization
        if (isTckRunning)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private val applicationConfiguration = ApplicationConfiguration()

    private val mediaTypeRegistry = MediaTypeCodecRegistry.of(
        JsonMediaTypeCodec(objectMapper, applicationConfiguration, null),
        JsonStreamMediaTypeCodec(objectMapper, applicationConfiguration, null)
    )

    override fun createHttpClient(baseUrl: String): HttpClient =
        HttpClientImpl(
            DefaultHttpClient(
                FixedLoadBalancer(URL(baseUrl)),
                httpClientConfiguration,
                null,
                null,
                sslFactory,
                mediaTypeRegistry,
                null
            ),
            getBasePath(baseUrl)
        )
}
