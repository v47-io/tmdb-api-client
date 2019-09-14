package io.v47.tmdb.http

import io.v47.tmdb.http.impl.HttpClientImpl
import org.springframework.context.ApplicationContext
import org.springframework.web.reactive.function.client.WebClient

class ContextWebClientFactory(private val applicationContext: ApplicationContext) : HttpClientFactory {
    override fun createHttpClient(baseUrl: String): HttpClient {
        return HttpClientImpl(
            applicationContext
                .getBean(WebClient.Builder::class.java)
                .baseUrl(baseUrl)
                .build()
        )
    }
}
