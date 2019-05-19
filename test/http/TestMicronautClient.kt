package io.v47.tmdb.http

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.core.annotation.AnnotationMetadataResolver
import io.micronaut.core.io.buffer.ByteBuffer
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.MediaType
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
import io.reactivex.Flowable
import io.v47.tmdb.api.ErrorResponse
import io.v47.tmdb.api.ErrorResponseException
import io.v47.tmdb.api.RawErrorResponse
import io.v47.tmdb.api.toErrorResponse
import io.v47.tmdb.http.HttpMethod.*
import io.v47.tmdb.http.impl.HttpResponseImpl
import io.v47.tmdb.utils.TypeInfo
import org.reactivestreams.Publisher
import java.net.URL
import java.net.URLEncoder
import io.micronaut.http.HttpRequest as MnHttpRequest
import io.micronaut.http.HttpResponse as MnHttpResponse
import io.micronaut.http.client.HttpClient as MnHttpClient

class TestMicronautClientFactory : HttpClientFactory {
    private val httpClientConfiguration = DefaultHttpClientConfiguration()
    private val threadFactory = DefaultThreadFactory(MultithreadEventLoopGroup::class.java)
    private val sslFactory = NettyClientSslBuilder(httpClientConfiguration.sslConfiguration)

    private val objectMapper = ObjectMapper().apply {
        findAndRegisterModules()
    }

    private val applicationConfiguration = ApplicationConfiguration()

    private val mediaTypeRegistry = MediaTypeCodecRegistry.of(
        JsonMediaTypeCodec(objectMapper, applicationConfiguration, null),
        JsonStreamMediaTypeCodec(objectMapper, applicationConfiguration, null)
    )

    override fun createHttpClient(baseUrl: String): HttpClient =
        TestMicronautClient(
            DefaultHttpClient(
                LoadBalancer.fixed(URL(baseUrl)),
                httpClientConfiguration,
                null,
                threadFactory,
                sslFactory,
                mediaTypeRegistry,
                AnnotationMetadataResolver.DEFAULT
            )
        )
}

private class TestMicronautClient(private val rawClient: MnHttpClient) : HttpClient {
    override fun <T : Any> execute(request: HttpRequest, responseType: TypeInfo): Publisher<HttpResponse<T>> {
        return if ((responseType as? TypeInfo.Simple)?.type == ByteArray::class.java)
            Flowable.fromPublisher(rawClient.exchange(request.toMnHttpRequest(false)))
                .map { resp ->
                    if (resp.code() == 200)
                        @Suppress("UNCHECKED_CAST")
                        resp.toHttpResponse() as HttpResponse<T>
                    else
                        throwErrorResponse(resp, request)
                }
        else {
            val argument = responseType.toArgument()

            Flowable.fromPublisher(rawClient.exchange(request.toMnHttpRequest()))
                .map { resp ->
                    if (resp.code() == 200)
                        @Suppress("UNCHECKED_CAST")
                        resp.toHttpResponse(argument) as HttpResponse<T>
                    else
                        throwErrorResponse(resp, request)
                }
        }
    }

    private fun throwErrorResponse(
        mnResponse: MnHttpResponse<ByteBuffer<Any>>,
        request: HttpRequest
    ): Nothing {
        val errorResponse =
            mnResponse.getBody(RawErrorResponse::class.java)
                .map { it.toErrorResponse() }
                .orElseGet {
                    val txt = mnResponse.body()?.let { body ->
                        val tmp = ByteArray(body.readableBytes())
                        body.read(tmp)
                        String(tmp, Charsets.UTF_8)
                    } ?: "Unidentified error"

                    ErrorResponse(txt, mnResponse.code())
                }!!

        throw ErrorResponseException(errorResponse, request)
    }

    private fun HttpRequest.toMnHttpRequest(json: Boolean = true): MnHttpRequest<*> {
        val uriSB = StringBuilder(url)
        if (query.isNotEmpty()) {
            uriSB.append("?")
            query
                .map { (name, values) ->
                    uriSB.append(URLEncoder.encode(name, Charsets.UTF_8))
                    uriSB.append('=')
                    var first = true
                    values.forEach { value ->
                        if (first)
                            first = false
                        else
                            uriSB.append(',')

                        uriSB.append(URLEncoder.encode(value.toString(), Charsets.UTF_8))
                    }
                }
        }

        return MnHttpRequest
            .create<Any>(
                when (method) {
                    Get -> HttpMethod.GET
                    Post -> HttpMethod.POST
                    Put -> HttpMethod.PUT
                    Delete -> HttpMethod.DELETE
                },
                uriSB.toString()
            )
            .accept(
                if (json)
                    MediaType.APPLICATION_JSON_TYPE
                else
                    MediaType.APPLICATION_OCTET_STREAM_TYPE
            )
            .body(body)
            .header(
                HttpHeaders.CONTENT_TYPE,
                if (body !is ByteArray)
                    MediaType.APPLICATION_JSON
                else
                    MediaType.APPLICATION_OCTET_STREAM
            )
    }

    private fun MnHttpResponse<ByteBuffer<Any>>.toHttpResponse(): HttpResponse<ByteArray> {
        val cl = if (contentLength > -1)
            contentLength
        else
            0

        val ba = if (cl > 0) {
            val tmp = ByteArray(cl.toInt())
            body()?.read(tmp)
            tmp
        } else
            null

        return HttpResponseImpl(
            code(),
            headers.associate { (key, value) -> key to value },
            ba
        )
    }

    private fun MnHttpResponse<out Any>.toHttpResponse(argument: Argument<*>) =
        HttpResponseImpl(
            code(),
            headers.associate { (key, value) -> key to value },
            getBody(argument).orElse(null)
        )

    override fun close() = rawClient.close()
}

private fun TypeInfo.toArgument() =
    when (this) {
        is TypeInfo.Simple -> toArgument()
        is TypeInfo.Generic -> toArgument()
    }

private fun TypeInfo.Simple.toArgument(): Argument<*> =
    Argument.of(type)

private fun TypeInfo.Generic.toArgument(): Argument<*> =
    Argument.of(
        if (type is TypeInfo.Simple)
            (type as TypeInfo.Simple).type
        else
            throw IllegalArgumentException("Cannot convert TypeInfo.Generic into Class<*>"),
        *typeArguments.map { it.toArgument() }.toTypedArray()
    )
