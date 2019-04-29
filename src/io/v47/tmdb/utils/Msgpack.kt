package io.v47.tmdb.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.msgpack.jackson.dataformat.MessagePackFactory

private val objectMapper = ObjectMapper(MessagePackFactory()).apply {
    findAndRegisterModules()
}

internal fun pack(value: Any): ByteArray =
    objectMapper.writeValueAsBytes(value)

internal fun <T : Any> unpack(value: ByteArray, targetType: TypeReference<T>): T =
    objectMapper.readValue(value, targetType)

internal inline fun <reified T : Any> unpack(value: ByteArray): T =
    unpack(value, jacksonTypeRef())
