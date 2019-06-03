package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Test

class ConfigurationTest : AbstractTmdbTest() {
    private val configuration by lazy { client.configuration }

    @Test
    fun testGetConfiguration() {
        configuration.system().blockingFirst()
    }

    @Test
    fun testGetCountries() {
        configuration.countries().blockingFirst()
    }

    @Test
    fun testGetJobs() {
        configuration.jobs().blockingFirst()
    }

    @Test
    fun testGetLanguages() {
        configuration.languages().blockingFirst()
    }

    @Test
    fun testGetPrimaryTranslations() {
        configuration.primaryTranslations().blockingFirst()
    }

    @Test
    fun testGetTimezones() {
        configuration.timezones().blockingFirst()
    }
}
