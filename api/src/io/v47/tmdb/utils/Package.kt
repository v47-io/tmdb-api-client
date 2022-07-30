/**
 * The Clear BSD License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
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
package io.v47.tmdb.utils

import io.v47.tmdb.model.Height
import io.v47.tmdb.model.ImageSize
import io.v47.tmdb.model.Original
import io.v47.tmdb.model.Width
import java.util.*

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
internal fun checkPage(page: Int) =
    require(page in pageRange) { "Invalid page: Pages start at 1 and end at 1000." }

fun List<ImageSize>.findClosestWidth(width: Int, forceSelectHigher: Boolean = true): ImageSize {
    val widthList = asSequence().filterIsInstance<Width>().sortedBy { it.value }.toList()
    val selectedIndex = Collections.binarySearch(widthList, Width(width))

    return if (selectedIndex < 0 || selectedIndex >= widthList.size)
        Original
    else if (forceSelectHigher && widthList[selectedIndex].value < width)
        widthList.getOrElse(selectedIndex + 1) { Original }
    else
        widthList[selectedIndex]
}

fun List<ImageSize>.findClosestHeight(height: Int, forceSelectHigher: Boolean = true): ImageSize {
    val heightList = asSequence().filterIsInstance<Height>().sortedBy { it.value }.toList()
    val selectedIndex = Collections.binarySearch(heightList, Height(height))

    return if (selectedIndex < 0 || selectedIndex >= heightList.size)
        Original
    else if (forceSelectHigher && heightList[selectedIndex].value < height)
        heightList.getOrElse(selectedIndex + 1) { Original }
    else
        heightList[selectedIndex]
}
