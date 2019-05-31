package io.v47.tmdb.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.core.annotation.AnnotationMetadataResolver
import io.micronaut.http.client.DefaultHttpClient
import io.micronaut.http.client.DefaultHttpClientConfiguration
import io.micronaut.http.client.LoadBalancer
import io.micronaut.http.client.ssl.NettyClientSslBuilder
import io.micronaut.http.codec.MediaTypeCodecRegistry
import io.micronaut.jackson.codec.JsonMediaTypeCodec
import io.micronaut.jackson.codec.JsonStreamMediaTypeCodec
import io.micronaut.runtime.ApplicationConfiguration
import io.netty.channel.MultithreadEventLoopGroup
import io.netty.util.concurrent.DefaultThreadFactory
import io.v47.tmdb.http.impl.HttpClientImpl
import io.v47.tmdb.http.utils.getBasePath
import java.net.URL
import io.micronaut.http.HttpMethod as MnHttpMethod
import io.micronaut.http.HttpRequest as MnHttpRequest
import io.micronaut.http.HttpResponse as MnHttpResponse
import io.micronaut.http.client.HttpClient as MnHttpClient

class StandaloneMnClientFactory : HttpClientFactory {
    private val httpClientConfiguration = DefaultHttpClientConfiguration()
    private val threadFactory = DefaultThreadFactory(MultithreadEventLoopGroup::class.java)
    private val sslFactory = NettyClientSslBuilder(httpClientConfiguration.sslConfiguration)

    private val objectMapper = ObjectMapper().apply {
        findAndRegisterModules()
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
                LoadBalancer.fixed(URL(baseUrl)),
                httpClientConfiguration,
                null,
                threadFactory,
                sslFactory,
                mediaTypeRegistry,
                AnnotationMetadataResolver.DEFAULT
            ),
            getBasePath(baseUrl)
        )
}
