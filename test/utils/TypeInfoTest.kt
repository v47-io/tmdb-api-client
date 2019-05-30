package io.v47.tmdb.utils

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
                listOf(TypeInfo.Simple(SimplePojo::class.java))
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
                        listOf(TypeInfo.Simple(SimplePojo::class.java))
                    )
                )
            ),
            typeInfo
        )
    }

    private data class SimplePojo(val willItBlend: Boolean)
}
