/**
 * The Clear BSD License
 *
 * Copyright (c) 2024, the tmdb-api-client authors
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

import io.v47.tmdb.model.ImageSize

object ImageSizeUtil {
    @JvmOverloads
    @JvmStatic
    fun <T : ImageSize> List<T>.findClosestWidth(width: Int, forceSelectHigher: Boolean = true): ImageSize {
        val widthList = asSequence()
            .filterIsInstance<ImageSize.Width>()
            .sortedBy { it.value }.toList()

        val selectedIndex = widthList.indexOfLast { it.value < width }

        return getClosestSize(width, forceSelectHigher, widthList, selectedIndex)
    }

    @JvmOverloads
    @JvmStatic
    fun <T : ImageSize> List<T>.findClosestHeight(height: Int, forceSelectHigher: Boolean = true): ImageSize {
        val heightList = asSequence()
            .filterIsInstance<ImageSize.Height>()
            .sortedBy { it.value }
            .toList()

        val selectedIndex = heightList.indexOfLast { it.value < height }

        return getClosestSize(height, forceSelectHigher, heightList, selectedIndex)
    }

    private fun getClosestSize(
        value: Int,
        forceSelectHigher: Boolean,
        sizeList: List<ImageSize>,
        selectedIndex: Int
    ) = when {
        selectedIndex < 0 -> sizeList.getOrElse(0) { ImageSize.Original }
        selectedIndex >= sizeList.size -> ImageSize.Original

        forceSelectHigher && sizeList[selectedIndex].value < value ->
            sizeList.getOrElse(selectedIndex + 1) { ImageSize.Original }

        else -> sizeList[selectedIndex]
    }
}
