/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.autoconfigure

import io.v47.tmdb.TmdbClient
import io.v47.tmdb.api.key.TmdbApiKeyProvider
import io.v47.tmdb.http.ContextWebClientFactory
import io.v47.tmdb.http.HttpClientFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration

private const val PROPERTY_EXPR = "\${tmdb-client.api-key:#{systemEnvironment['TMDB_API_KEY']}}"

/**
 * Autoconfigures a [TmdbClient] bean if it doesn't already exist.
 */
@Configuration
@Conditional(TmdbAutoConfigurationCondition::class)
class TmdbAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(HttpClientFactory::class)
    fun contextWebClientFactory(applicationContext: ApplicationContext): HttpClientFactory =
        ContextWebClientFactory(applicationContext)

    @Bean
    @ConditionalOnMissingBean(TmdbApiKeyProvider::class)
    @ConditionalOnExpression("'$PROPERTY_EXPR'!=''")
    fun apiKeyProvider(
        @Value(PROPERTY_EXPR)
        apiKey: String
    ): TmdbApiKeyProvider =
        TmdbApiKeyProvider { apiKey }

    @Bean
    @ConditionalOnMissingBean(TmdbClient::class)
    fun tmdbClient(
        httpClientFactory: HttpClientFactory,
        apiKeyProvider: TmdbApiKeyProvider
    ) =
        TmdbClient(httpClientFactory, apiKeyProvider)
}
