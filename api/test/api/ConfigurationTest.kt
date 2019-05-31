package io.v47.tmdb.api

import io.reactivex.Flowable
import org.junit.jupiter.api.Test

class ConfigurationTest : AbstractTmdbTest() {
    private val configuration by lazy { client.configuration }

    @Test
    fun testGetConfiguration() {
        Flowable.fromPublisher(configuration.system()).blockingFirst()
    }

    @Test
    fun testGetCountries() {
        Flowable.fromPublisher(configuration.countries()).blockingFirst()
    }

    @Test
    fun testGetJobs() {
        Flowable.fromPublisher(configuration.jobs()).blockingFirst()
    }

    @Test
    fun testGetLanguages() {
        Flowable.fromPublisher(configuration.languages()).blockingFirst()
    }

    @Test
    fun testGetPrimaryTranslations() {
        Flowable.fromPublisher(configuration.primaryTranslations()).blockingFirst()
    }

    @Test
    fun testGetTimezones() {
        Flowable.fromPublisher(configuration.timezones()).blockingFirst()
    }
}
