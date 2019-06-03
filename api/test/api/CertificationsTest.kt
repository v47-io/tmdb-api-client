package io.v47.tmdb.api

import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CertificationsTest : AbstractTmdbTest() {
    @Test
    fun `get movie certifications`() {
        val movieCertifications = client.certifications.forMovies().blockingFirst()
        assertTrue(movieCertifications.certifications.isNotEmpty())
    }

    @Test
    fun `get tv certifications`() {
        val tvCertifications = client.certifications.forTv().blockingFirst()
        assertTrue(tvCertifications.certifications.isNotEmpty())
    }
}
