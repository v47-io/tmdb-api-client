/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
        private val PART_RELEASE_DATE: LocalDate = LocalDate.parse("1977-05-25")

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
