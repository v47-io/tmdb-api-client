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
package io.v47.tmdb.jackson.deserialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import io.v47.tmdb.jackson.TmdbApiModule
import io.v47.tmdb.model.CreditMedia
import io.v47.tmdb.model.CreditMediaMovie
import io.v47.tmdb.model.CreditPersonKnownFor
import io.v47.tmdb.model.CreditPersonKnownForMovie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MediaTypeBasedDeserializerTest {
    private val objectMapper = ObjectMapper()
        .findAndRegisterModules()
        .registerModule(TmdbApiModule())

    @Test
    fun `it deserializes a mapped media type`() {
        val value = objectMapper.readValue(
            """{"media_type":"Movie"}""",
            CreditPersonKnownFor::class.java
        )

        assertEquals(CreditPersonKnownForMovie::class.java, value.javaClass)
    }

    @Test
    fun `it deserializes a custom-detected media type`() {
        val value = objectMapper.readValue(
            """{"title":"A movie"}""",
            CreditMedia::class.java
        )

        assertEquals(CreditMediaMovie::class.java, value.javaClass)
    }

    @Test
    fun `it reports invalid mapped media types`() {
        val exception = assertThrows(JsonParseException::class.java) {
            objectMapper.readValue(
                """{"media_type":"music"}""",
                CreditPersonKnownFor::class.java
            )
        }

        assertEquals(
            "Tried to parse CreditPersonKnownFor: Invalid value for 'media_type': music",
            exception.originalMessage
        )
    }

    @Test
    fun `it reports missing custom type detectors`() {
        val exception = assertThrows(JsonParseException::class.java) {
            objectMapper.readValue("{}", CreditPersonKnownFor::class.java)
        }

        assertEquals(
            "Tried to parse CreditPersonKnownFor: No custom type detector provided!",
            exception.originalMessage
        )
    }

    @Test
    fun `it reports when a custom type cannot be detected`() {
        val exception = assertThrows(JsonParseException::class.java) {
            objectMapper.readValue("{}", CreditMedia::class.java)
        }

        assertEquals(
            "Tried to parse: CreditMedia: Failed to detect type!",
            exception.originalMessage
        )
    }
}
