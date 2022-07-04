/**
 * Copyright 2022 The tmdb-api-client Authors
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

class KeywordsTest : AbstractTmdbTest() {
    companion object {
        const val KEYWORD_ID = 1553
        const val KEYWORD_NAME = "pottery"
    }

    @Test
    fun testGetKeyword() {
        val result = client.keyword.details(KEYWORD_ID).blockingFirst()
        assertEquals(KEYWORD_NAME, result.name)
    }

    @Test
    fun getKeywordMovies() {
        val result = client.keyword.movies(KEYWORD_ID).blockingFirst()
        assertTrue(result.results.isNotEmpty())
    }
}
