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

import com.fasterxml.jackson.annotation.JsonValue
import io.v47.tmdb.utils.isValidCountryCode
import io.v47.tmdb.utils.isValidLanguageCode
import io.v47.tmdb.utils.isValidLocaleTag
import io.v47.tmdb.utils.requireCountryCode
import io.v47.tmdb.utils.requireLanguageCode
import io.v47.tmdb.utils.requireLocaleCode
import java.util.IllformedLocaleException
import java.util.Locale

@JvmInline
value class LocaleCode(@get:JsonValue val value: String) {
    val isValid: Boolean
        get() = isValidLocaleTag(value)

    val language: LanguageCode?
        get() = toLocaleOrNull()?.let(LanguageCode::fromLocale)

    val country: CountryCode?
        get() = toLocaleOrNull()?.let(CountryCode::fromLocale)

    val script: String?
        get() = toLocaleOrNull()?.script?.takeIf(String::isNotEmpty)

    val region: String?
        get() = toLocaleOrNull()?.country?.takeIf(String::isNotEmpty)

    fun normalized(): LocaleCode {
        requireLocaleCode(value)
        return LocaleCode(requireNotNull(toLocaleOrNull()).toLanguageTag())
    }

    fun toLocaleOrNull(): Locale? {
        if (!isValid) return null
        return try {
            Locale.Builder().setLanguageTag(value).build()
        } catch (_: IllformedLocaleException) {
            null
        }
    }

    fun displayName(displayLocale: Locale = Locale.ENGLISH): String? =
        toLocaleOrNull()?.getDisplayName(displayLocale)?.ifEmpty { value }

    fun withCountry(country: CountryCode?): LocaleCode {
        requireLocaleCode(value)
        country?.let { requireCountryCode(it.value) }

        val builder = Locale.Builder().setLanguageTag(value)
        builder.setRegion(country?.value.orEmpty())
        return LocaleCode(builder.build().toLanguageTag())
    }

    override fun toString(): String = value

    companion object {
        @JvmStatic
        fun parse(value: String): LocaleCode {
            requireLocaleCode(value)
            return LocaleCode(value)
        }

        @JvmStatic
        fun parseOrNull(value: String): LocaleCode? =
            if (isValidLocaleTag(value)) LocaleCode(value) else null

        @JvmStatic
        fun fromLocale(locale: Locale): LocaleCode = LocaleCode(locale.toLanguageTag())
    }
}

@JvmInline
value class LanguageCode(@get:JsonValue val value: String) {
    val isValid: Boolean
        get() = isValidLanguageCode(value)

    fun normalized(): LanguageCode {
        requireLanguageCode(value)
        return LanguageCode(value.lowercase(Locale.ROOT))
    }

    fun toLocaleOrNull(): Locale? {
        if (!isValid) return null
        return try {
            Locale.Builder().setLanguage(value).build()
        } catch (_: IllformedLocaleException) {
            null
        }
    }

    fun displayName(displayLocale: Locale = Locale.ENGLISH): String? =
        toLocaleOrNull()?.getDisplayLanguage(displayLocale)?.ifEmpty { value }

    fun toLocaleCode(country: CountryCode? = null): LocaleCode {
        requireLanguageCode(value)
        country?.let { requireCountryCode(it.value) }

        val builder = Locale.Builder().setLanguage(value)
        country?.let { builder.setRegion(it.value) }
        return LocaleCode(builder.build().toLanguageTag())
    }

    override fun toString(): String = value

    companion object {
        @JvmStatic
        fun parse(value: String): LanguageCode {
            requireLanguageCode(value)
            return LanguageCode(value)
        }

        @JvmStatic
        fun parseOrNull(value: String): LanguageCode? =
            if (isValidLanguageCode(value)) LanguageCode(value) else null

        @JvmStatic
        fun fromLocale(locale: Locale): LanguageCode? =
            locale.language.takeIf(::isValidLanguageCode)?.let(::LanguageCode)
    }
}

@JvmInline
value class CountryCode(@get:JsonValue val value: String) {
    val isValid: Boolean
        get() = isValidCountryCode(value)

    fun normalized(): CountryCode {
        requireCountryCode(value)
        return CountryCode(value.uppercase(Locale.ROOT))
    }

    fun toLocaleOrNull(): Locale? {
        if (!isValid) return null
        return try {
            Locale.Builder().setRegion(value).build()
        } catch (_: IllformedLocaleException) {
            null
        }
    }

    fun toLocale(): Locale {
        requireCountryCode(value)
        return requireNotNull(toLocaleOrNull())
    }

    fun displayName(displayLocale: Locale = Locale.ENGLISH): String? =
        toLocaleOrNull()?.getDisplayCountry(displayLocale)?.ifEmpty { value }

    override fun toString(): String = value

    companion object {
        @JvmStatic
        fun parse(value: String): CountryCode {
            requireCountryCode(value)
            return CountryCode(value)
        }

        @JvmStatic
        fun parseOrNull(value: String): CountryCode? =
            if (isValidCountryCode(value)) CountryCode(value) else null

        @JvmStatic
        fun fromLocale(locale: Locale): CountryCode? =
            locale.country.takeIf(::isValidCountryCode)?.let(::CountryCode)
    }
}
