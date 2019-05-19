package io.v47.tmdb.api

import io.reactivex.Flowable
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CertificationsTest : AbstractTmdbTest() {
    @Test
    fun `get movie certifications`() {
        val movieCertifications = Flowable.fromPublisher(client.certifications.forMovies()).blockingFirst()
        assertTrue(movieCertifications.certifications.isNotEmpty())
    }

    @Test
    fun `get tv certifications`() {
        val tvCertifications = Flowable.fromPublisher(client.certifications.forTv()).blockingFirst()
        assertTrue(tvCertifications.certifications.isNotEmpty())
    }
}
