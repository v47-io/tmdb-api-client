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
package io.v47.tmdb.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.v47.tmdb.model.Country
import io.v47.tmdb.model.CountryCode
import io.v47.tmdb.model.CreditMedia
import io.v47.tmdb.model.CreditMediaMovie
import io.v47.tmdb.model.LanguageCode
import io.v47.tmdb.model.LocaleCode
import io.v47.tmdb.model.MovieDetails
import io.v47.tmdb.model.MovieListResult
import io.v47.tmdb.model.TranslationListResult
import io.v47.tmdb.model.TvListResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CodeMappingTest {
    private val objectMapper = ObjectMapper()
        .findAndRegisterModules()
        .registerModule(TmdbApiModule())

    @Test
    fun `code values and lists map as scalar strings`() {
        val language = objectMapper.readValue("\"CN\"", LanguageCode::class.java)
        val languages = objectMapper.readValue(
            """["CN", "qaa"]""",
            object : TypeReference<List<LanguageCode>>() {}
        )
        val locale = objectMapper.readValue("\"zh-Hant-TW\"", LocaleCode::class.java)
        val country = objectMapper.readValue("\"ZZ\"", CountryCode::class.java)

        assertEquals(LanguageCode("CN"), language)
        assertEquals(listOf(LanguageCode("CN"), LanguageCode("qaa")), languages)
        assertEquals(LocaleCode("zh-Hant-TW"), locale)
        assertEquals(CountryCode("ZZ"), country)
        assertEquals("\"CN\"", objectMapper.writeValueAsString(language))
        assertEquals("[\"CN\",\"qaa\"]", objectMapper.writeValueAsString(languages))
        assertEquals("\"ZZ\"", objectMapper.writeValueAsString(country))
    }

    @Test
    fun `model code properties preserve unknown empty and nullable values`() {
        val unknown = objectMapper.readValue(
            """{"original_language":"CN"}""",
            MovieListResult::class.java
        )
        val empty = objectMapper.readValue(
            """{"original_language":""}""",
            MovieListResult::class.java
        )
        val nullable = objectMapper.readValue(
            """{"original_language":null}""",
            MovieListResult::class.java
        )

        assertEquals(LanguageCode("CN"), unknown.originalLanguage)
        assertEquals("CN", unknown.originalLanguage?.value)
        assertEquals(LanguageCode(""), empty.originalLanguage)
        assertNull(nullable.originalLanguage)
        assertEquals("CN", objectMapper.readTree(objectMapper.writeValueAsString(unknown))
            .get("original_language").asText())
    }

    @Test
    fun `movie details preserve lowercase cn through a serialized round trip`() {
        val movie = objectMapper.readValue(
            """{"original_language":"cn"}""",
            MovieDetails::class.java
        )

        assertEquals(LanguageCode("cn"), movie.originalLanguage)

        val json = objectMapper.writeValueAsString(movie)
        assertEquals("cn", objectMapper.readTree(json).get("original_language").asText())
        assertEquals(movie, objectMapper.readValue(json, MovieDetails::class.java))
    }

    @Test
    fun `translation locale composition handles valid missing and invalid components`() {
        val complete = objectMapper.readValue(
            """{"iso_639_1":"qaa","iso_3166_1":"ZZ","name":"name"}""",
            TranslationListResult::class.java
        )
        val languageOnly = objectMapper.readValue(
            """{"iso_639_1":"qaa"}""",
            TranslationListResult::class.java
        )
        val invalidCountry = objectMapper.readValue(
            """{"iso_639_1":"qaa","iso_3166_1":"USA"}""",
            TranslationListResult::class.java
        )
        val invalidLanguage = objectMapper.readValue(
            """{"iso_639_1":"qaaa","iso_3166_1":"US"}""",
            TranslationListResult::class.java
        )
        val missingLanguage = objectMapper.readValue(
            """{"iso_3166_1":"US"}""",
            TranslationListResult::class.java
        )

        assertEquals(LocaleCode("qaa-ZZ"), complete.locale)
        assertEquals(LocaleCode("qaa"), languageOnly.locale)
        assertNull(invalidCountry.locale)
        assertNull(invalidLanguage.locale)
        assertNull(missingLanguage.locale)

        val json = objectMapper.writeValueAsString(complete)
        assertFalse(objectMapper.readTree(json).has("locale"))
        assertEquals("qaa", objectMapper.readTree(json).get("iso_639_1").asText())
        assertEquals(complete, objectMapper.readValue(json, TranslationListResult::class.java))
    }

    @Test
    fun `configuration and tv country fields retain typed scalar values and defaults`() {
        val country = objectMapper.readValue(
            """{"iso_3166_1":"qz"}""",
            Country::class.java
        )
        val tv = objectMapper.readValue(
            """{"origin_country":["ZZ"],"networks":[{"origin_country":"qz"}]}""",
            TvListResult::class.java
        )
        val emptyTv = objectMapper.readValue("{}", TvListResult::class.java)

        assertEquals(CountryCode("qz"), country.code)
        assertEquals(listOf(CountryCode("ZZ")), tv.originCountry)
        assertEquals(CountryCode("qz"), tv.networks.single().originCountry)
        assertEquals(emptyList<CountryCode>(), emptyTv.originCountry)
        assertTrue(emptyTv.networks.isEmpty())
        assertTrue(objectMapper.writeValueAsString(tv).contains("\"origin_country\":[\"ZZ\"]"))
    }

    @Test
    fun `polymorphic credit models retain unknown language codes`() {
        val media = objectMapper.readValue(
            """{"title":"A movie","original_language":"CN"}""",
            CreditMedia::class.java
        )

        assertTrue(media is CreditMediaMovie)
        assertEquals(LanguageCode("CN"), media.originalLanguage)
    }
}
