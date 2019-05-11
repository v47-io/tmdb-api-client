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

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer

internal abstract class MediaTypeBasedDeserializer<T : Any>(
    private val mapping: Map<String, Class<*>>,
    private val clazz: Class<T>,
    private val customDetector: ((JsonNode) -> Class<out T>?)? = null
) : StdNodeBasedDeserializer<T>(clazz) {

    override fun convert(root: JsonNode, ctxt: DeserializationContext): T {
        val targetType = if (root.hasNonNull("media_type")) {
            val mediaType = root.get("media_type").textValue().toLowerCase()
            mapping[mediaType]
                ?: throw JsonParseException(
                    ctxt.parser, "Tried to parse ${clazz.simpleName}: " +
                            "Invalid value for 'media_type': $mediaType"
                )
        } else {
            customDetector
                ?: throw JsonParseException(
                    ctxt.parser, "Tried to parse ${clazz.simpleName}: " +
                            "No custom type detector provided!"
                )

            val tmp = customDetector.invoke(root)
                ?: throw JsonParseException(
                    ctxt.parser, "Tried to parse: ${clazz.simpleName}: " +
                            "Failed to detect type!"
                )

            tmp
        }

        val jacksonType = ctxt.typeFactory.constructType(targetType)
        val deserializer = ctxt.findRootValueDeserializer(jacksonType)
        val nodeParser = root.traverse(ctxt.parser.codec)
        nodeParser.nextToken()

        @Suppress("UNCHECKED_CAST")
        return deserializer.deserialize(nodeParser, ctxt) as T
    }
}
