/**
 * Copyright 2022 The tmdb-api-v2 Authors
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
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode

internal class OriginalLanguageDeserializer : StdDeserializer<LanguageCode?>(LanguageCode::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LanguageCode? {
        return if (p.currentToken() == JsonToken.VALUE_STRING) {
            val str = p.valueAsString
            val languageCode = LanguageCode.getByCode(str)
                ?: CountryCode.getByAlpha2Code(str.uppercase())?.toLocale()?.let { LanguageCode.valueOf(it.language) }

            if (languageCode == LanguageCode.undefined)
                null
            else
                languageCode
        } else
            null
    }
}
