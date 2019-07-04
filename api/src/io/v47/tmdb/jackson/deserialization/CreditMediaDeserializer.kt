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

import io.v47.tmdb.model.CreditMedia
import io.v47.tmdb.model.CreditMediaMovie
import io.v47.tmdb.model.CreditMediaTv

internal class CreditMediaDeserializer : MediaTypeBasedDeserializer<CreditMedia>(
    emptyMap(),
    CreditMedia::class.java,
    { jsonNode ->
        when {
            jsonNode.hasNonNull("title") -> CreditMediaMovie::class.java
            jsonNode.hasNonNull("name") -> CreditMediaTv::class.java
            else -> null
        }
    }
)