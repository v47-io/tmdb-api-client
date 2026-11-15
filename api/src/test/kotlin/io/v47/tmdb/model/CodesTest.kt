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
package io.v47.tmdb.model

import java.util.Locale
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CodesTest {
    @Test
    fun `locale components use JDK parsing and preserve the original value`() {
        val code = LocaleCode.parse("zh-Hant-TW")

        assertEquals("zh-Hant-TW", code.value)
        assertEquals(LanguageCode("zh"), code.language)
        assertEquals(CountryCode("TW"), code.country)
        assertEquals("Hant", code.script)
        assertEquals("TW", code.region)
    }

    @Test
    fun `locale tags support numeric regions and absent components`() {
        val numericRegion = LocaleCode.parse("en-419")
        assertNull(numericRegion.country)
        assertEquals("419", numericRegion.region)
        assertNull(LocaleCode.parse("en").country)
        assertNull(LocaleCode.parse("en").script)
        assertNull(LocaleCode.parse("x-private").language)
        assertNull(LocaleCode.fromLocale(Locale.ROOT).language)
        assertEquals("und", LocaleCode.fromLocale(Locale.ROOT).value)
    }

    @Test
    fun `withCountry retains locale scripts variants and extensions`() {
        val original = LocaleCode.parse("de-Latn-DE-POSIX-u-ca-gregory")

        assertEquals(
            "de-Latn-US-POSIX-u-ca-gregory",
            original.withCountry(CountryCode.parse("US")).value
        )
        assertEquals("de-Latn-POSIX-u-ca-gregory", original.withCountry(null).value)
    }

    @Test
    fun `parsing validates without normalizing or trimming`() {
        assertEquals("EN-us", LocaleCode.parse("EN-us").value)
        assertEquals("Zh", LanguageCode.parse("Zh").value)
        assertEquals("us", CountryCode.parse("us").value)
        assertThrows(IllegalArgumentException::class.java) { LocaleCode.parse(" ") }
        assertThrows(IllegalArgumentException::class.java) { LocaleCode.parse("en_US") }
        assertThrows(IllegalArgumentException::class.java) { LanguageCode.parse("en-US") }
        assertThrows(IllegalArgumentException::class.java) { CountryCode.parse("419") }
        assertNull(LocaleCode.parseOrNull("en_US"))
        assertNull(LanguageCode.parseOrNull("en-US"))
        assertNull(CountryCode.parseOrNull("419"))
    }

    @Test
    fun `raw wrappers preserve unfamiliar empty and malformed values`() {
        val unfamiliar = LocaleCode("qaa-ZZ")
        val empty = LanguageCode("")
        val malformed = CountryCode("USA")

        assertEquals("qaa-ZZ", unfamiliar.value)
        assertEquals("", empty.toString())
        assertEquals("USA", malformed.value)
        assertFalse(empty.isValid)
        assertFalse(malformed.isValid)
        assertNull(LocaleCode("en_US").toLocaleOrNull())
        assertNull(LocaleCode("en_US").displayName())
        assertThrows(IllegalArgumentException::class.java) { LocaleCode("en_US").normalized() }
        assertThrows(IllegalArgumentException::class.java) { LanguageCode("eng-US").toLocaleCode() }
        assertThrows(IllegalArgumentException::class.java) { CountryCode("USA").normalized() }
    }

    @Test
    fun `normalization is explicit and independent of default locale`() {
        val originalDefault = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr"))

            assertEquals("en-US", LocaleCode.parse("EN-us").normalized().value)
            assertEquals("is", LanguageCode.parse("IS").normalized().value)
            assertEquals("TR", CountryCode.parse("tr").normalized().value)
            assertEquals("EN-us", LocaleCode.parse("EN-us").value)
        } finally {
            Locale.setDefault(originalDefault)
        }
    }

    @Test
    fun `locale conversion display names and composition delegate to JDK`() {
        val displayLocale = Locale.FRENCH
        val language = LanguageCode.parse("en")
        val country = CountryCode.parse("US")
        val localeCode = language.toLocaleCode(country)

        assertEquals("en-US", localeCode.value)
        assertEquals(Locale.US, localeCode.toLocaleOrNull())
        assertEquals("en", LanguageCode.fromLocale(Locale.US)?.value)
        assertEquals("US", CountryCode.fromLocale(Locale.US)?.value)
        assertEquals(Locale.Builder().setRegion("US").build(), country.toLocale())
        assertEquals(
            localeCode.toLocaleOrNull()?.getDisplayName(displayLocale),
            localeCode.displayName(displayLocale)
        )
        assertEquals(
            language.toLocaleOrNull()?.getDisplayLanguage(displayLocale),
            language.displayName(displayLocale)
        )
        assertEquals(
            country.toLocaleOrNull()?.getDisplayCountry(displayLocale),
            country.displayName(displayLocale)
        )
        assertNull(LanguageCode.fromLocale(Locale.ROOT))
        assertNull(CountryCode.fromLocale(Locale.ENGLISH))
        assertTrue(LocaleCode.fromLocale(Locale.US).isValid)
    }
}
