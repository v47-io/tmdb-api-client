/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.jackson.deserialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode

internal class EnumDeserializerModifier : BeanDeserializerModifier() {
    override fun modifyEnumDeserializer(
        config: DeserializationConfig,
        type: JavaType,
        beanDesc: BeanDescription,
        deserializer: JsonDeserializer<*>
    ): JsonDeserializer<*> {
        return EnhancedEnumDeserializer(type)
    }

    @Suppress("UNCHECKED_CAST")
    private class EnhancedEnumDeserializer(val type: JavaType) : JsonDeserializer<Enum<*>>() {
        @Suppress("ReturnCount")
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Enum<*> {
            val rawClass = (type.rawClass as Class<Enum<*>>)
            val intVal = p.valueAsInt
            val strVal = p.valueAsString
            val isString = intVal == 0 && strVal != "0"
            val enumConstants = rawClass.enumConstants
            if (isString) {
                val lc = strVal.lowercase()
                val found = enumConstants.find { it.name == lc }
                if (found != null)
                    return found

                if ('_' in lc) {
                    val pc = convertToPascalCase(lc)
                    val foundPc = enumConstants.find { it.name == pc }
                    if (foundPc != null)
                        return foundPc
                }

                val uc = strVal.uppercase()
                return enumConstants.find { it.name.uppercase() == uc } ?: when (rawClass) {
                    CountryCode::class.java -> CountryCode.UNDEFINED
                    LanguageCode::class.java -> LanguageCode.undefined
                    else -> throw invalidFormatException(p, rawClass, strVal)
                }
            } else return enumConstants.find { it.ordinal == intVal }
                ?: throw invalidFormatException(p, rawClass, strVal)
        }

        private fun convertToPascalCase(value: String) =
            value.split('_').joinToString("") { it[0].uppercase() + it.substring(1) }

        private fun invalidFormatException(p: JsonParser, rawClass: Class<*>, value: Any) =
            InvalidFormatException(
                p, "value not found in ${
                    rawClass.enumConstants.joinToString(
                        prefix = "[", postfix = "]"
                    )
                }", value, rawClass
            )
    }
}
