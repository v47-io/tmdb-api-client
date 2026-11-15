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
package io.v47.tmdb.utils

import java.util.IllformedLocaleException
import java.util.Locale

private const val MIN_LANGUAGE_CODE_LENGTH = 2
private const val MAX_LANGUAGE_CODE_LENGTH = 3

internal fun isValidLocaleTag(value: String): Boolean {
    if (value.isBlank()) return false

    return try {
        Locale.Builder().setLanguageTag(value).build()
        true
    } catch (_: IllformedLocaleException) {
        false
    }
}

internal fun isValidLanguageCode(value: String): Boolean =
    value.length in MIN_LANGUAGE_CODE_LENGTH..MAX_LANGUAGE_CODE_LENGTH &&
        value.all { it in 'a'..'z' || it in 'A'..'Z' }

internal fun isValidCountryCode(value: String): Boolean =
    value.length == 2 && value.all { it in 'a'..'z' || it in 'A'..'Z' }

internal fun requireLocaleCode(value: String): String {
    require(isValidLocaleTag(value)) { "Invalid locale code: $value" }
    return value
}

internal fun requireLanguageCode(value: String): String {
    require(isValidLanguageCode(value)) { "Invalid language code: $value" }
    return value
}

internal fun requireCountryCode(value: String): String {
    require(isValidCountryCode(value)) { "Invalid country code: $value" }
    return value
}
