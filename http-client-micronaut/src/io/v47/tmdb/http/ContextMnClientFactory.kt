package io.v47.tmdb.http

import io.micronaut.context.BeanContext
import io.micronaut.http.client.HttpClientConfiguration
import io.micronaut.http.client.LoadBalancer
import io.v47.tmdb.http.impl.HttpClientImpl
import io.v47.tmdb.http.utils.getBasePath
import java.net.URL
import io.micronaut.http.client.HttpClient as MnHttpClient

class ContextMnClientFactory(private val beanContext: BeanContext) : HttpClientFactory {
    override fun createHttpClient(baseUrl: String): HttpClient {
        val url = URL(baseUrl)
        val portPart = if (url.port > -1)
            ":${url.port}"
        else
            ""

        return HttpClientImpl(
            beanContext.createBean(
                MnHttpClient::class.java,
                mapOf(
                    "loadBalancer" to LoadBalancer.fixed(URL("${url.protocol}://${url.host}$portPart")),
                    "configuration" to beanContext.getBean(HttpClientConfiguration::class.java)
                )
            ),
            getBasePath(baseUrl)
        )
    }
}
