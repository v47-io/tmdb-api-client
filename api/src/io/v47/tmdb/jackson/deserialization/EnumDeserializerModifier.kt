/**
 * Copyright 2019 Alex Katlein <dev@vemilyus.com>
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
package io.v47.tmdb.jackson.deserialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
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
                val lc = strVal.toLowerCase()
                val found = enumConstants.find { it.name == lc }
                if (found != null)
                    return found

                val uc = strVal.toUpperCase()
                return enumConstants.find { it.name.toUpperCase() == uc }
                    ?: when (rawClass) {
                        CountryCode::class.java -> CountryCode.UNDEFINED
                        LanguageCode::class.java -> LanguageCode.undefined
                        else -> throw invalidFormatException(p, rawClass, strVal)
                    }
            } else
                return enumConstants.find { it.ordinal == intVal }
                    ?: throw invalidFormatException(p, rawClass, strVal)
        }

        private fun invalidFormatException(p: JsonParser, rawClass: Class<*>, value: Any): InvalidFormatException =
            InvalidFormatException(
                p,
                "value not found in ${rawClass.enumConstants.joinToString(prefix = "[", postfix = "]")}",
                value,
                rawClass
            )
    }
}
