package io.v47.tmdb.utils

import io.v47.tmdb.model.Height
import io.v47.tmdb.model.ImageSize
import io.v47.tmdb.model.Original
import io.v47.tmdb.model.Width
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList

internal fun String.urlEncode() =
    URLEncoder.encode(this, Charsets.UTF_8)

/**
 * Matches strings conforming to this pattern: `([a-z]{2})(?:-([A-Z]{2}))?`
 */
internal val ISO_639_1 = Regex("([a-z]{2})(?:-([A-Z]{2}))?")

internal val ISO_3166_1 = Regex("[A-Z]{2}")

/**
 * The range of valid page numbers for paginated API methods: 1 through 1000 (inclusive)
 */
@Suppress("MagicNumber")
private val pageRange = 1..1000

/**
 * Checks if the specified page number is in the range of valid numbers,
 * as described by [pageRange].
 *
 * @throws IllegalArgumentException if that is not the case
 */
internal fun checkPage(page: Int) {
    if (page !in pageRange)
        throw IllegalArgumentException("Invalid page: Pages start at 1 and max at 1000.")
}

fun List<ImageSize>.findClosestWidth(width: Int, forceSelectHigher: Boolean = true): ImageSize {
    val widthList = ArrayList(asSequence().filterIsInstance<Width>().sortedBy { it.value }.toList())
    val selectedIndex = Collections.binarySearch(widthList, Width(width))

    return if (selectedIndex < 0 || selectedIndex >= widthList.size)
        Original
    else if (forceSelectHigher && widthList[selectedIndex].value < width)
        widthList.getOrElse(selectedIndex + 1) { Original }
    else
        widthList[selectedIndex]
}

fun List<ImageSize>.findClosestHeight(height: Int, forceSelectHigher: Boolean = true): ImageSize {
    val heightList = ArrayList(asSequence().filterIsInstance<Height>().sortedBy { it.value }.toList())
    val selectedIndex = Collections.binarySearch(heightList, Height(height))

    return if (selectedIndex < 0 || selectedIndex >= heightList.size)
        Original
    else if (forceSelectHigher && heightList[selectedIndex].value < height)
        heightList.getOrElse(selectedIndex + 1) { Original }
    else
        heightList[selectedIndex]
}
