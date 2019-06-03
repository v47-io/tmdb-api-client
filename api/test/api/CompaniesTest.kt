package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CompaniesTest : AbstractTmdbTest() {
    companion object {
        private const val COMPANY_ID = 2

        // Matching values
        private const val COMPANY_HEADQUARTERS = "Burbank, California, United States"
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
