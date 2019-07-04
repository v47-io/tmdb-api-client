package io.v47.tmdb.http.impl

import io.micronaut.core.io.buffer.ByteBuffer
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.http.api.toErrorResponse
import io.v47.tmdb.http.utils.toArgument
import io.v47.tmdb.utils.TypeInfo
import org.reactivestreams.Publisher
import java.net.URLEncoder
import io.micronaut.http.HttpRequest as MnHttpRequest
import io.micronaut.http.HttpResponse as MnHttpResponse
import io.micronaut.http.client.HttpClient as MnHttpClient

internal class HttpClientImpl(private val rawClient: MnHttpClient, private val basePath: String = "") : HttpClient {
    private val byteBufferArgument = Argument.of(ByteBuffer::class.java)
    private val imageErrorRegex = Regex("""<h1>(.+?)</h1>""", RegexOption.IGNORE_CASE)

    override fun execute(request: HttpRequest, responseType: TypeInfo): Publisher<HttpResponse<out Any>> {
        val jsonBody = (responseType as? TypeInfo.Simple)?.type != ByteArray::class.java
        val argument = if (jsonBody) responseType.toArgument() else null

        return Flowable.fromPublisher(
            rawClient.exchange(
                request.toMnHttpRequest(jsonBody),
                byteBufferArgument,
                byteBufferArgument
            )
        ).onErrorReturn { t ->
            @Suppress("UNCHECKED_CAST")
            if (t is HttpClientResponseException)
                t.response as MnHttpResponse<ByteBuffer<*>>
            else
                throw IllegalArgumentException("Not a HttpClientResponseException", t)
        }.map { resp ->
            if (resp.code() == HttpStatus.OK.code)
                @Suppress("UNCHECKED_CAST")
                resp.toHttpResponse(argument)
            else
                HttpResponseImpl(
                    resp.code(),
                    resp.headers.associate { (key, value) -> key to value },
                    createErrorResponse(resp)
                )
        }
    }

    private fun createErrorResponse(mnResponse: MnHttpResponse<ByteBuffer<*>>): ErrorResponse =
        mnResponse.getBody(RawErrorResponse::class.java)
            .map { it.toErrorResponse() }
            .orElseGet {
                val txt = mnResponse.getBody(ByteBuffer::class.java).map { body ->
                    val tmp = ByteArray(body.readableBytes())
                    body.read(tmp)
                    val str = String(tmp, Charsets.UTF_8)

                    val imageErrorMatch = imageErrorRegex.matchEntire(str)
                    if (imageErrorMatch != null)
                        imageErrorMatch.groupValues[1]
                    else
                        str
                }.orElseGet(null) ?: "Unidentified error"

                ErrorResponse(txt, mnResponse.code())
            }

    private fun HttpRequest.toMnHttpRequest(json: Boolean = true): MnHttpRequest<*> =
        MnHttpRequest
            .create<Any>(
                when (method) {
                    HttpMethod.Get -> io.micronaut.http.HttpMethod.GET
                    HttpMethod.Post -> io.micronaut.http.HttpMethod.POST
                    HttpMethod.Put -> io.micronaut.http.HttpMethod.PUT
                    HttpMethod.Delete -> io.micronaut.http.HttpMethod.DELETE
                },
                createUri()
            )
            .accept(
                if (json)
                    MediaType.APPLICATION_JSON_TYPE
                else
                    MediaType.ALL_TYPE
            )
            .body(body)
            .header(
                HttpHeaders.CONTENT_TYPE,
                if (body !is ByteArray)
                    MediaType.APPLICATION_JSON
                else
                    MediaType.APPLICATION_OCTET_STREAM
            )

    private fun HttpRequest.createUri(): String {
        val uriSB = StringBuilder(basePath)
        if (!url.startsWith("/"))
            uriSB.append('/')

        uriSB.append(url)

        if (query.isNotEmpty()) {
            uriSB.append("?")

            var first = true
            query.map { (name, values) ->
                if (first)
                    first = false
                else
                    uriSB.append('&')

                uriSB.append(URLEncoder.encode(name, Charsets.UTF_8))
                uriSB.append('=')
                var valueFirst = true
                values.forEach { value ->
                    if (valueFirst)
                        valueFirst = false
                    else
                        uriSB.append(',')

                    uriSB.append(URLEncoder.encode(value.toString(), Charsets.UTF_8))
                }
            }
        }

        return uriSB.toString()
    }

    private fun MnHttpResponse<*>.toHttpResponse(argument: Argument<*>?) =
        if (argument != null)
            HttpResponseImpl(
                code(),
                headers.associate { (key, value) -> key to value },
                getBody(argument).orElse(null)
            )
        else {
            val cl = if (contentLength > -1)
                contentLength
            else
                0

            val ba = if (cl > 0) {
                val tmp = ByteArray(cl.toInt())
                (body() as? ByteBuffer<*>)?.read(tmp)
                tmp
            } else
                null

            HttpResponseImpl(
                code(),
                headers.associate { (key, value) -> key to value },
                ba
            )
        }

    override fun close() = rawClient.close()
}