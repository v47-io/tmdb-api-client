package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DiscoverTest : AbstractTmdbTest() {
    @Test
    fun testGetMovie() {
        client.discover.movies {
            language(LocaleCode.de_DE)
            releaseDate(LocalDate.of(2017, 1, 1)..LocalDate.MAX)
        }.blockingFirst()
    }

    @Test
    fun testGetTv() {
        client.discover.tv {
            language(LocaleCode.de_DE)
            firstAirDate(LocalDate.of(2017, 1, 1)..LocalDate.MAX)
        }.blockingFirst()
    }
}
