package io.v47.tmdb.http.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.http.api.toErrorResponse
import io.v47.tmdb.utils.TypeInfo
import org.reactivestreams.Publisher
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

internal class HttpClientImpl(private val rawClient: WebClient) : HttpClient {
    private val om = ObjectMapper().apply {
        findAndRegisterModules()
    }

    private val imageErrorRegex = Regex("""<h1>(.+?)</h1>""", RegexOption.IGNORE_CASE)

    override fun execute(request: HttpRequest, responseType: TypeInfo): Publisher<HttpResponse<out Any>> {
        val jsonBody = (responseType as? TypeInfo.Simple)?.type != ByteArray::class.java

        val requestSpec = request.toRequestSpec(jsonBody)

        return Flowable.fromPublisher(requestSpec.exchange())
            .switchMap { resp ->
                if (resp.statusCode() == HttpStatus.OK) {
                    val typeReference = ParameterizedTypeReference.forType<Any>(responseType.fullType)
                    val bodyFlux = resp.bodyToFlux(typeReference)

                    Flowable.fromPublisher(bodyFlux)
                        .map { resp to it }
                } else {
                    val bodyFlux = resp.bodyToMono(ByteArray::class.java)

                    Flowable.fromPublisher(bodyFlux)
                        .map { resp to readErrorBody(it, resp.rawStatusCode()) }
                }
            }
            .map { (resp, body) ->
                HttpResponseImpl(
                    resp.rawStatusCode(),
                    resp.headers().asHttpHeaders().toMap(),
                    body
                ) as HttpResponse<out Any>
            }
            .onErrorReturn { t ->
                if (t is HttpClientErrorException)
                    HttpResponseImpl(
                        t.rawStatusCode,
                        t.responseHeaders?.toMap() ?: emptyMap(),
                        createErrorResponse(t)
                    )
                else
                    throw IllegalArgumentException("Not a HttpClientErrorException", t)
            }
    }

    @Suppress("ComplexMethod")
    private fun HttpRequest.toRequestSpec(jsonBody: Boolean): WebClient.RequestHeadersSpec<*> {
        val reqSpec = when (method) {
            HttpMethod.Get -> rawClient.get()
            HttpMethod.Post -> rawClient.post()
            HttpMethod.Put -> rawClient.put()
            HttpMethod.Delete -> rawClient.delete()
        }

        reqSpec.uri { uriBuilder ->
            uriBuilder.path(url)

            query.forEach { (name, values) ->
                uriBuilder.queryParam(name, values.joinToString(","))
            }

            uriBuilder.build()
        }
            .accept(
                if (jsonBody)
                    MediaType.APPLICATION_JSON
                else
                    MediaType.ALL
            )

        val body = body

        val optBodyReqSpec = if (reqSpec is WebClient.RequestBodyUriSpec && body != null)
            reqSpec.body(BodyInserters.fromValue(body))
        else
            reqSpec

        return optBodyReqSpec
            .header(
                HttpHeaders.CONTENT_TYPE,
                if (body !is ByteArray)
                    MediaType.APPLICATION_JSON_VALUE
                else
                    MediaType.APPLICATION_OCTET_STREAM_VALUE
            )
    }

    private fun createErrorResponse(t: HttpClientErrorException): ErrorResponse {
        val bodyByteArray = t.responseBodyAsByteArray

        return readErrorBody(bodyByteArray, t.rawStatusCode)
    }

    private fun readErrorBody(bodyByteArray: ByteArray, status: Int) =
        runCatching { om.readValue(bodyByteArray, RawErrorResponse::class.java) }
            .map { it.toErrorResponse() }
            .getOrElse {
                val txt = String(bodyByteArray, Charsets.UTF_8).let { str ->
                    val imageErrorMatch = imageErrorRegex.matchEntire(str)
                    if (imageErrorMatch != null)
                        imageErrorMatch.groupValues[1]
                    else
                        str
                }

                ErrorResponse(txt, status)
            }

    override fun close() = Unit
}
