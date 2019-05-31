package io.v47.tmdb.api

import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Network
import io.v47.tmdb.model.NetworkAlternativeNames
import io.v47.tmdb.model.NetworkImages

class NetworksApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the details of a network
     *
     * @param networkId The id of the network
     */
    fun details(networkId: Int) =
        httpExecutor.execute(get<Network>("/network/$networkId"))

    /**
     * Get the alternative names of a network
     *
     * @param networkId The id of the network
     */
    fun alternativeNames(networkId: Int) =
        httpExecutor.execute(get<NetworkAlternativeNames>("/network/$networkId/alternative_names"))

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
        httpExecutor.execute(get<NetworkImages>("/network/$networkId/images"))
}
