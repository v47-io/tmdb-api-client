/**
 * The Clear BSD License
 *
 * Copyright (c) 2026, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the copyright holder nor the names of its
 *        contributors may be used to endorse or promote products derived from this
 *        software without specific prior written permission.
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
package io.v47.tmdb.api

import io.v47.tmdb.TmdbClient
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.HttpRequest
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.TypeInfo
import io.v47.tmdb.model.CountryCode
import io.v47.tmdb.model.LanguageCode
import io.v47.tmdb.model.LocaleCode
import java.util.concurrent.Flow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CodeQueryParametersTest {
    @Test
    fun `localized helpers preserve valid spelling omit null and reject malformed values`() {
        val factory = CapturingHttpClientFactory()
        val client = TmdbClient(factory, "offline")

        client.movie.details(42, LocaleCode("EN-us"))
        assertEquals("EN-us", factory.requests.last().query["language"]?.single())

        client.movie.upcoming(page = 2, language = LocaleCode("fr-CA"))
        assertEquals("fr-CA", factory.requests.last().query["language"]?.single())
        assertEquals("2", factory.requests.last().query["page"]?.single())

        client.movie.details(42)
        assertFalse(factory.requests.last().query.containsKey("language"))

        assertThrows(IllegalArgumentException::class.java) {
            client.movie.details(42, LocaleCode("en_US"))
        }
    }

    @Test
    fun `discovery locale setters validate and preserve their value`() {
        val factory = CapturingHttpClientFactory()
        val client = TmdbClient(factory, "offline")

        client.discover.movies { language(LocaleCode("EN-us")) }
        assertEquals("EN-us", factory.requests.last().query["language"]?.single())

        assertThrows(IllegalArgumentException::class.java) {
            client.discover.tv { language(LocaleCode("en_US")) }
        }
    }

    @Test
    fun `language country and certification filters validate and replace`() {
        val factory = CapturingHttpClientFactory()
        val client = TmdbClient(factory, "offline")

        client.discover.movies {
            originalLanguage(LanguageCode("qaa"))
            originalLanguage(LanguageCode("zz"))
            region(CountryCode("us"))
            region(CountryCode("ZZ"))
            certificationCountry(CountryCode("qz"))
            certificationCountry(CountryCode("XX"))
        }

        val discoveryQuery = factory.requests.last().query
        assertEquals("zz", discoveryQuery["with_original_language"]?.single())
        assertEquals("ZZ", discoveryQuery["region"]?.single())
        assertEquals("XX", discoveryQuery["certification_country"]?.single())

        client.movie.alternativeTitles(1, CountryCode("us"))
        assertEquals("us", factory.requests.last().query["country"]?.single())

        client.search.forMovies("title", region = CountryCode("qz"))
        assertEquals("qz", factory.requests.last().query["region"]?.single())

        val primaryTranslations: Flow.Publisher<List<LocaleCode>> =
            client.configuration.primaryTranslations()
        assertNotNull(primaryTranslations)
    }

    @Test
    fun `language and country filters reject malformed raw wrappers`() {
        val factory = CapturingHttpClientFactory()
        val client = TmdbClient(factory, "offline")

        assertThrows(IllegalArgumentException::class.java) {
            client.discover.movies { originalLanguage(LanguageCode("en-US")) }
        }
        assertThrows(IllegalArgumentException::class.java) {
            client.discover.tv { originalLanguage(LanguageCode("e1")) }
        }
        assertThrows(IllegalArgumentException::class.java) {
            client.discover.movies { region(CountryCode("USA")) }
        }
        assertThrows(IllegalArgumentException::class.java) {
            client.discover.movies { certificationCountry(CountryCode("U1")) }
        }
        assertThrows(IllegalArgumentException::class.java) {
            client.movie.alternativeTitles(1, CountryCode("419"))
        }
        assertThrows(IllegalArgumentException::class.java) {
            client.search.forMovies("title", region = CountryCode("U_S"))
        }
        assertTrue(factory.requests.isEmpty())
    }

    @Test
    fun `all image endpoints retain deduplicated language values and null marker`() {
        val factory = CapturingHttpClientFactory()
        val client = TmdbClient(factory, "offline")
        val languages = listOf(LanguageCode("en"), null, LanguageCode("en"), LanguageCode("EN"))

        client.movie.images(1, LocaleCode("fr-ca"), languages)
        assertImageLanguages(factory.requests.last(), "fr-ca")

        client.tvShow.images(1, null, languages)
        assertImageLanguages(factory.requests.last(), null)

        client.tvEpisode.images(1, 2, 3, null, languages)
        assertImageLanguages(factory.requests.last(), null)

        client.tvSeason.images(1, 2, null, languages)
        assertImageLanguages(factory.requests.last(), null)
    }

    @Test
    fun `image endpoints reject invalid raw language wrappers`() {
        val factory = CapturingHttpClientFactory()
        val client = TmdbClient(factory, "offline")

        assertThrows(IllegalArgumentException::class.java) {
            client.movie.images(1, includeLanguage = listOf(LanguageCode("en-US")))
        }
        assertTrue(factory.requests.isEmpty())
    }

    private fun assertImageLanguages(request: HttpRequest, language: String?) {
        if (language == null)
            assertFalse(request.query.containsKey("language"))
        else
            assertEquals(language, request.query["language"]?.single())

        assertEquals(listOf("en", "null", "EN"), request.query["include_image_language"])
    }

    private class CapturingHttpClientFactory : HttpClientFactory {
        val requests = mutableListOf<HttpRequest>()

        override fun createHttpClient(baseUrl: String): HttpClient = object : HttpClient {
            override fun execute(
                request: HttpRequest,
                responseType: TypeInfo
            ): Flow.Publisher<HttpResponse<out Any>> {
                requests.add(request)
                return Flow.Publisher { subscriber ->
                    subscriber.onSubscribe(object : Flow.Subscription {
                        override fun request(n: Long) {
                            subscriber.onComplete()
                        }

                        override fun cancel() = Unit
                    })
                }
            }
        }
    }
}
