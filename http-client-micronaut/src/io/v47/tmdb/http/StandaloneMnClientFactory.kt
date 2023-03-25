/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.DefaultApplicationContextBuilder
import io.micronaut.context.env.DefaultEnvironment
import io.micronaut.core.annotation.AnnotationMetadataResolver
import io.micronaut.core.io.ResourceResolver
import io.micronaut.http.client.DefaultHttpClientConfiguration
import io.micronaut.http.client.LoadBalancer
import io.micronaut.http.client.netty.DefaultHttpClient
import io.micronaut.http.client.netty.ssl.NettyClientSslBuilder
import io.micronaut.http.codec.MediaTypeCodecRegistry
import io.micronaut.jackson.databind.JacksonDatabindMapper
import io.micronaut.json.codec.JsonMediaTypeCodec
import io.micronaut.json.codec.JsonStreamMediaTypeCodec
import io.micronaut.runtime.ApplicationConfiguration
import io.v47.tmdb.http.impl.HttpClientImpl
import io.v47.tmdb.http.utils.getBasePath
import java.net.URI
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

    private val jsonMapper = JacksonDatabindMapper(
        ObjectMapper().apply {
            findAndRegisterModules()

            // Don't need to be so strict for TCK, it's testing the implementation of the client,
            // not the deserialization
            if (isTckRunning)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    )

    private val applicationConfiguration = ApplicationConfiguration()

    private val mediaTypeRegistry = MediaTypeCodecRegistry.of(
        JsonMediaTypeCodec(jsonMapper, applicationConfiguration, null),
        JsonStreamMediaTypeCodec(jsonMapper, applicationConfiguration, null)
    )

    override fun createHttpClient(baseUrl: String): HttpClient =
        HttpClientImpl(
            DefaultHttpClient(
                LoadBalancer.fixed(URI(baseUrl)),
                httpClientConfiguration,
                null,
                null,
                sslFactory,
                mediaTypeRegistry,
                AnnotationMetadataResolver.DEFAULT,
                emptyList(),
                DefaultEnvironment(object : DefaultApplicationContextBuilder() {})
            ),
            getBasePath(baseUrl)
        )
}
