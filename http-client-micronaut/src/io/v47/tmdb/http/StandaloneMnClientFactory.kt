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
