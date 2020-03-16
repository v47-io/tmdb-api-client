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
package io.v47.tmdb.model

import org.slf4j.LoggerFactory
import java.io.Serializable

abstract class TmdbType(private vararg val ignoredProperties: String) : Serializable {
    private val logger by lazy { LoggerFactory.getLogger(javaClass)!! }

    @Suppress("unused")
    fun handleUnknownProperty(key: String, value: Any?) {
        if (key != "@class" && key !in ignoredProperties)
            logger.trace("Unknown property: '$key' -> '$value'")
    }
}

interface TmdbIntId {
    val id: Int?
}

interface TmdbStringId {
    val id: String?
}
