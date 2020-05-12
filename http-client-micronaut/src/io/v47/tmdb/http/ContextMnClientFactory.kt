/**
 * Copyright 2020 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.v47.tmdb.http

import io.micronaut.context.BeanContext
import io.micronaut.http.client.HttpClientConfiguration
import io.micronaut.http.client.LoadBalancer
import io.micronaut.http.client.filter.HttpClientFilterResolver
import io.v47.tmdb.http.impl.HttpClientImpl
import io.v47.tmdb.http.utils.getBasePath
import java.net.URL
import io.micronaut.http.client.HttpClient as MnHttpClient

class ContextMnClientFactory(private val beanContext: BeanContext) : HttpClientFactory {
    override fun createHttpClient(baseUrl: String): HttpClient {
        return HttpClientImpl(
            beanContext.createBean(
                MnHttpClient::class.java,
                mapOf(
                    "loadBalancer" to LoadBalancer.fixed(URL(baseUrl)),
                    "configuration" to beanContext.getBean(HttpClientConfiguration::class.java),
                    "filterResolver" to beanContext.getBean(HttpClientFilterResolver::class.java)
                )
            ),
            getBasePath(baseUrl)
        )
    }
}
