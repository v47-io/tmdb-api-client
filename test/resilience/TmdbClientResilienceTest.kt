package io.v47.tmdb.resilience

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.impl.HttpResponseImpl
import io.v47.tmdb.utils.vavr.invoke
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

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

    @BeforeAll
    fun init() {
        httpClient = mock {
            on { execute(simpleHttpRequest) } doReturn Flowable.just(simpleHttpResponse as HttpResponse)
            on { execute(pojoListHttpRequest) } doReturn Flowable.just(pojoListHttpResponse as HttpResponse)
        }

        resilience = TmdbClientResilience(httpClient, objectMapper, null, null, null, null, null)
    }

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
}

private data class SimplePojo(val someString: String, val anInt: Int)
