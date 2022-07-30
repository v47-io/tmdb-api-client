/**
 * The Clear BSD License
 *
 * Copyright (c) 2022 the tmdb-api-client authors
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

import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Network
import io.v47.tmdb.model.NetworkAlternativeNames
import io.v47.tmdb.model.NetworkImages

class NetworksApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get the details of a network
     *
     * @param networkId The id of the network
     */
    fun details(networkId: Int) =
        http.get<Network>("/network/{networkId}") {
            pathVar("networkId", networkId)
        }


    /**
     * Get the alternative names of a network
     *
     * @param networkId The id of the network
     */
    fun alternativeNames(networkId: Int) =
        http.get<NetworkAlternativeNames>("/network/{networkId}/alternative_names") {
            pathVar("networkId", networkId)
        }


    /**
     * Get a networks logos by id.
     *
     * There are two image formats that are supported for networks, PNG's and SVG's.
     * You can see which type the original file is by looking at the `file_type` field.
     * We prefer SVG's as they are resolution independent and as such, the width and
     * height are only there to reflect the original asset that was uploaded. An SVG
     * can be scaled properly beyond those dimensions if you call them as a PNG.
     *
     * For more information about how SVG's and PNG's can be used, take a read through
     * [this document](https://developers.themoviedb.org/3/getting-started/images)
     *
     * @param networkId The id of the network
     */
    fun images(networkId: Int) =
        http.get<NetworkImages>("/network/{networkId}/images") {
            pathVar("networkId", networkId)
        }

}
