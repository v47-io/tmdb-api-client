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
package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NetworksTest : AbstractTmdbTest() {
    companion object {
        const val TNT_ID = 41
        const val TNT_NAME = "TNT"
        const val TNT_ALTERNATIVE_NAME = "Turner Network Television"
    }

    @Test
    fun testGetDetails() {
        val details = client.network.details(TNT_ID).blockingFirst()
        assertEquals(TNT_NAME, details.name)
    }

    @Test
    fun testGetAlternativeNames() {
        val alternativeNames = client.network.alternativeNames(TNT_ID).blockingFirst()
        assertEquals(TNT_ALTERNATIVE_NAME, alternativeNames.results.firstOrNull()?.name)
    }

    @Test
    fun testGetImages() {
        val images = client.network.images(TNT_ID).blockingFirst()
        assertTrue(images.logos.isNotEmpty())
    }
}
