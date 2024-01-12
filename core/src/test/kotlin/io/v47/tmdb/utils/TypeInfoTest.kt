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
package io.v47.tmdb.utils

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.v47.tmdb.http.TypeInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TypeInfoTest {
    @Test
    fun `create simple type info`() {
        val typeReference = tmdbTypeReference<SimplePojo>()
        val typeInfo = typeReference.toTypeInfo()

        assertEquals(TypeInfo.Simple(SimplePojo::class.java), typeInfo)
    }

    @Test
    fun `create generic type info`() {
        val typeReference = tmdbTypeReference<List<SimplePojo>>()
        val typeInfo = typeReference.toTypeInfo()

        assertEquals(
            TypeInfo.Generic(
                List::class.java,
                listOf(TypeInfo.Simple(SimplePojo::class.java)),
                jacksonTypeRef<List<SimplePojo>>().type
            ),
            typeInfo
        )
    }

    @Test
    fun `create nested generic type info`() {
        val typeReference = tmdbTypeReference<Map<String, List<SimplePojo>>>()
        val typeInfo = typeReference.toTypeInfo()

        assertEquals(
            TypeInfo.Generic(
                Map::class.java,
                listOf(
                    TypeInfo.Simple(String::class.java),
                    TypeInfo.Generic(
                        List::class.java,
                        listOf(TypeInfo.Simple(SimplePojo::class.java)),
                        jacksonTypeRef<List<SimplePojo>>().type
                    )
                ),
                jacksonTypeRef<Map<String, List<SimplePojo>>>().type
            ),
            typeInfo
        )
    }

    private data class SimplePojo(val willItBlend: Boolean)
}
