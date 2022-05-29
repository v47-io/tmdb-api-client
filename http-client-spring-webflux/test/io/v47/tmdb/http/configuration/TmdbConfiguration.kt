/**
 * Copyright 2022 The tmdb-api-v2 Authors
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
package io.v47.tmdb.http.configuration

import io.v47.tmdb.TmdbClient
import io.v47.tmdb.http.ContextWebClientFactory
import io.v47.tmdb.http.HttpClientFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean

@TestConfiguration
class TmdbConfiguration {
    @Value("#{environment.API_KEY}")
    private lateinit var apiKey: String

    @Bean
    fun contextWebClientFactory(applicationContext: ApplicationContext): HttpClientFactory =
        ContextWebClientFactory(applicationContext)

    @Bean
    fun tmdbClient(httpClientFactory: HttpClientFactory) =
        TmdbClient(httpClientFactory, apiKey)
}
