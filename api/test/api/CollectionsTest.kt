package io.v47.tmdb.api

import com.neovisionaries.i18n.LanguageCode
import io.v47.tmdb.utils.blockingFirst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CollectionsTest : AbstractTmdbTest() {
    companion object {
        private const val COLLECTION_ID = 10 // Star Wars Collection

        // Matching values
        private const val COLLECTION_NAME = "Star Wars Collection"

        private const val PART_ID = 11
        private const val PART_ORIGINAL_TITLE = "Star Wars"
        private val PART_RELEASE_DATE = LocalDate.parse("1977-05-25")!!

        private const val MANDARIN_COLLECTION_TITLE = "星球大战（系列）"
    }

    @Test
    fun testGetCollectionDetails() {
        val collection = client.collection.details(COLLECTION_ID).blockingFirst()

        assertEquals(COLLECTION_ID, collection.id)
        assertEquals(COLLECTION_NAME, collection.name)

        val part = collection.parts.find { it.id == PART_ID }

        assertNotNull(part)

        assertEquals(PART_ORIGINAL_TITLE, part?.originalTitle)
        assertEquals(PART_RELEASE_DATE, part?.releaseDate)
    }

    @Test
    fun testGetCollectionImages() {
        val images = client.collection.images(COLLECTION_ID).blockingFirst()

        assertEquals(COLLECTION_ID, images.id)
    }

    @Test
    fun testGetCollectionTranslations() {
        val translations = client.collection.translations(COLLECTION_ID).blockingFirst()

        val chineseTranslation = translations.translations.find { it.language == LanguageCode.zh }
        assertNotNull(chineseTranslation)
        assertEquals(chineseTranslation?.data?.title, MANDARIN_COLLECTION_TITLE)
    }
}
