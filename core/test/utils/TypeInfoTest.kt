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
package io.v47.tmdb.utils

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
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
