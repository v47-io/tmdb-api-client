package io.v47.tmdb.resilience

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import io.github.resilience4j.cache.Cache
import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.impl.HttpResponseImpl
import io.v47.tmdb.utils.vavr.invoke
import io.vavr.CheckedFunction0
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import java.io.IOException
import javax.cache.CacheManager
import javax.cache.Caching
import javax.cache.configuration.MutableConfiguration

class TmdbClientResilienceWithCacheTest {
    private lateinit var cacheManager: CacheManager

    private lateinit var httpClient: HttpClient
    private lateinit var resilience: TmdbClientResilience

    private var failingMode = false

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

    @Test
    fun `second request execution returns cached response`() {
        val decoratedRequest = resilience.decorate(simpleHttpRequest)

        execute(decoratedRequest, simpleHttpResponse)

        failingMode = true
        execute(decoratedRequest, simpleHttpResponse)
    }

    private fun execute(
        request: CheckedFunction0<Publisher<HttpResponse>>,
        expectedResult: HttpResponse
    ) {
        val responsePublisher = request()
        val response = Flowable.fromPublisher(responsePublisher).blockingLast()

        Assertions.assertEquals(expectedResult, response)
    }

    @BeforeEach
    fun init() {
        val cachingProvider = Caching.getCachingProvider()
        cacheManager = cachingProvider.cacheManager
        val config = MutableConfiguration<String, ByteArray>()
            .setTypes(String::class.java, ByteArray::class.java)
            .setStoreByValue(true);

        httpClient = mock {
            on { execute(simpleHttpRequest) } doAnswer {
                if (failingMode)
                    throw IOException("connection lost")
                else
                    Flowable.just(simpleHttpResponse)
            }
        }

        resilience = TmdbClientResilience(
            httpClient,
            ObjectMapper().apply { findAndRegisterModules() },
            null,
            null,
            null,
            Cache.of(cacheManager.createCache("tmdb-api-cache", config)),
            null
        )

        failingMode = false
    }

    @AfterEach
    fun destroy() {
        cacheManager.close()
    }
}
