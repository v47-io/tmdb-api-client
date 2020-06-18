/**
 * Copyright 2020 The tmdb-api-v2 Authors
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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CompaniesTest : AbstractTmdbTest() {
    companion object {
        private const val COMPANY_ID = 2

        // Matching values
        private const val COMPANY_HEADQUARTERS = "Burbank, California"
        private const val COMPANY_NAME = "Walt Disney Pictures"

        private const val COMPANY_ALTERNATIVE_NAME = "Disney"
    }

    @Test
    fun testGetCompany() {
        val company = client.company.details(COMPANY_ID).blockingFirst()

        assertEquals(COMPANY_HEADQUARTERS, company.headquarters)
        assertEquals(COMPANY_NAME, company.name)

        assertNull(company.parentCompany)
    }

    @Test
    fun testGetAlternativeNames() {
        val alternativeNames = client.company.alternativeNames(COMPANY_ID).blockingFirst()

        assertEquals(alternativeNames.results.firstOrNull()?.name, COMPANY_ALTERNATIVE_NAME)
    }

    @Test
    fun testGetImages() {
        val images = client.company.images(COMPANY_ID).blockingFirst()

        assertTrue(images.logos.isNotEmpty())
    }
}
