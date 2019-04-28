package io.v47.tmdb.resilience

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.impl.HttpResponseImpl
import io.v47.tmdb.utils.vavr.invoke
import io.vavr.CheckedFunction0
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.reactivestreams.Publisher
import java.io.IOException
import java.util.concurrent.TimeoutException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TmdbClientResilienceTest {
    private val objectMapper = ObjectMapper().apply { findAndRegisterModules() }

    private lateinit var httpClient: HttpClient
    private lateinit var resilience: TmdbClientResilience

    private val someString = "This is just a totally random string!"
    private val someStringBytes = someString.toByteArray()

    private val simpleHttpRequest = HttpRequestImpl(HttpMethod.Get, "http://mocked.com/simple")
    private val simpleHttpResponse = HttpResponseImpl(
        200,
        mapOf(
            "content-type" to listOf("text/plain"),
            "content-length" to listOf("${someStringBytes.size}")
        ),
        someStringBytes
    )

    private val pojoList = listOf(
        SimplePojo("First String", 1),
        SimplePojo("Second String", 2),
        SimplePojo("Third String", 3)
    )
    private val pojoListBytes = objectMapper.writeValueAsBytes(pojoList)

    private val pojoListHttpRequest = HttpRequestImpl(HttpMethod.Get, "http://mocked.com/pojo-list")
    private val pojoListHttpResponse = HttpResponseImpl(
        200,
        mapOf(
            "content-type" to listOf("application/json"),
            "content-length" to listOf("${pojoListBytes.size}")
        ),
        pojoListBytes
    )

    private val longRunningHttpRequest = HttpRequestImpl(HttpMethod.Get, "http://mocked.com/long-running")

    private val recoveringHttpRequest = HttpRequestImpl(HttpMethod.Get, "http://mocked.com/recovering")
    private var recoveringExecuted = false

    private val emptyHttpRequest = HttpRequestImpl(HttpMethod.Get, "http://mocked.com/empty-request")
    private val emptyHttpResponse = HttpResponseImpl(204)

    @Test
    fun `simple http response is returned correctly`() {
        val decoratedRequest = resilience.decorate(simpleHttpRequest)
        val responsePublisher = decoratedRequest()
        val response = Flowable.fromPublisher(responsePublisher).blockingFirst()

        assertEquals(simpleHttpResponse, response)
    }

    @Test
    fun `pojo list http response is returned correctly`() {
        val decoratedRequest = resilience.decorate(pojoListHttpRequest, jacksonTypeRef<SimplePojo>())
        val responsePublisher = decoratedRequest()
        val response = Flowable.fromPublisher(responsePublisher).blockingIterable()

        assertIterableEquals(pojoList, response.map { it.second })
    }

    @Test
    fun `request times out correctly`() {
        val decoratedRequest = resilience.decorate(longRunningHttpRequest)
        val responsePublisher = decoratedRequest()

        val result = runCatching {
            Flowable.fromPublisher(responsePublisher).blockingLast()
        }

        assertTrue(result.isFailure && result.exceptionOrNull()?.cause is TimeoutException)
    }

    @Test
    fun `request is retried correctly`() {
        val decoratedRequest = resilience.decorate(recoveringHttpRequest)
        val responsePublisher = decoratedRequest()

        val response = Flowable.fromPublisher(responsePublisher).blockingFirst()

        assertEquals(simpleHttpResponse, response)
    }

    @Test
    fun `request is correctly rate-limited`() {
        val decoratedRequest = resilience.decorate(simpleHttpRequest)

        execute(decoratedRequest, simpleHttpResponse)
        execute(decoratedRequest, simpleHttpResponse)
        execute(decoratedRequest, simpleHttpResponse)
        execute(decoratedRequest, simpleHttpResponse)
        execute(decoratedRequest, simpleHttpResponse)
        execute(decoratedRequest, simpleHttpResponse)
        execute(decoratedRequest, simpleHttpResponse)
        execute(decoratedRequest, simpleHttpResponse)
    }

    @Test
    fun `empty http response is read correctly`() {
        val rawDecoratedRequest = resilience.decorate(emptyHttpRequest)
        val rawResponsePublisher = rawDecoratedRequest()

        val rawResponse = Flowable.fromPublisher(rawResponsePublisher).blockingFirst()

        assertEquals(emptyHttpResponse, rawResponse, "The empty raw HTTP response is invalid")

        val decoratedRequest = resilience.decorate(emptyHttpRequest, jacksonTypeRef<Unit>())
        val responsePublisher = decoratedRequest()

        val response = Flowable.fromPublisher(responsePublisher).blockingFirst()

        assertEquals(emptyHttpResponse, response.first, "The empty processed HTTP response is invalid")
    }

    private fun execute(
        request: CheckedFunction0<Publisher<HttpResponse>>,
        expectedResult: HttpResponse
    ) {
        val responsePublisher = request()
        val response = Flowable.fromPublisher(responsePublisher).blockingFirst()

        assertEquals(expectedResult, response)
    }

    @BeforeAll
    fun init() {
        httpClient = mock {
            on { execute(simpleHttpRequest) } doReturn Flowable.just(simpleHttpResponse as HttpResponse)
            on { execute(pojoListHttpRequest) } doReturn Flowable.just(pojoListHttpResponse as HttpResponse)
            on { execute(longRunningHttpRequest) } doAnswer {
                Thread.sleep(5000)
                throw IllegalArgumentException("UNREACHABLE")
            }
            on { execute(recoveringHttpRequest) } doAnswer {
                if (recoveringExecuted)
                    Flowable.just(simpleHttpResponse)
                else {
                    recoveringExecuted = true
                    throw IOException("connection lost")
                }
            }
            on { execute(emptyHttpRequest) } doReturn Flowable.just(emptyHttpResponse as HttpResponse)
        }

        resilience = TmdbClientResilience(httpClient, objectMapper, null, null, null, null, null)
    }
}

private data class SimplePojo(val someString: String, val anInt: Int)
