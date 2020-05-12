package io.v47.tmdb.jackson.deserialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode

class OriginalLanguageDeserializer : StdDeserializer<LanguageCode?>(LanguageCode::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LanguageCode? {
        return if (p.currentToken() == JsonToken.VALUE_STRING) {
            val str = p.valueAsString
            val languageCode = LanguageCode.getByCode(str)
                ?: CountryCode.getByAlpha2Code(str.toUpperCase())?.toLocale()?.let { LanguageCode.valueOf(it.language) }

            if (languageCode == LanguageCode.undefined)
                null
            else
                languageCode
        } else
            null
    }
}
