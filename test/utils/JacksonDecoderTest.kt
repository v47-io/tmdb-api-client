package io.v47.tmdb.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.reactivex.Flowable
import io.v47.tmdb.utils.jackson.JacksonDecoder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JacksonDecoderTest {
    private val objectMapper = ObjectMapper().apply { findAndRegisterModules() }

    private val singlePojo = Person("Anton", 32)
    private val singlePojoBytes = objectMapper.writeValueAsBytes(singlePojo)

    private val pojoList = listOf(
            Person("Peter", 24),
            Person("Tina", 22),
            Person("Michael", 14),
            Person("Sabrina", 12)
    )
    private val pojoListBytes = objectMapper.writeValueAsBytes(pojoList)

    private lateinit var jacksonDecoder: JacksonDecoder

    @BeforeAll
    fun init() {
        jacksonDecoder = JacksonDecoder(objectMapper)
    }

    @Test
    fun `deserializing single object`() {
        val resultPublisher = jacksonDecoder.decode(Flowable.just(singlePojoBytes), jacksonTypeRef<Person>())
        val resultPojo = Flowable.fromPublisher(resultPublisher).blockingLast()

        assertEquals(singlePojo, resultPojo)
    }

    @Test
    fun `deserializing stream of objects`() {
        val resultPublisher = jacksonDecoder.decode(Flowable.just(pojoListBytes), jacksonTypeRef<Person>())
        val resultPojos = Flowable.fromPublisher(resultPublisher).blockingIterable()

        assertIterableEquals(pojoList, resultPojos)
    }

    @Test
    fun `deserializing array of objects`() {
        val resultPublisher = jacksonDecoder.decode(Flowable.just(pojoListBytes), jacksonTypeRef<List<Person>>())
        val resultList = Flowable.fromPublisher(resultPublisher).blockingLast()

        assertIterableEquals(pojoList, resultList)
    }
}

private data class Person(val name: String, val age: Int)
