package io.v47.tmdb.utils

/**
 * Matches strings conforming to this pattern: `([a-z]{2})(?:-([A-Z]{2}))?`
 */
internal val ISO_639_1 = Regex("([a-z]{2})(?:-([A-Z]{2}))?")

internal val ISO_3166_1 = Regex("[A-Z]{2}")

/**
 * The range of valid page numbers for paginated API methods: 1 through 1000 (inclusive)
 */
private val PAGE_RANGE = 1..1000

/**
 * Checks if the specified page number is in the range of valid numbers,
 * as described by [PAGE_RANGE].
 *
 * @throws IllegalArgumentException if that is not the case
 */
internal fun checkPage(page: Int) {
    if (page !in PAGE_RANGE)
        throw IllegalArgumentException("Invalid page: Pages start at 1 and max at 1000.")
}
