/*
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
package io.v47.tmdb.http.quarkus

import com.neovisionaries.i18n.LocaleCode
import io.smallrye.mutiny.Uni
import io.v47.tmdb.TmdbClient
import io.v47.tmdb.api.MovieRequest
import io.v47.tmdb.model.MovieDetails
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
class MovieInfoResource(private val tmdbClient: TmdbClient) {
    @GET
    @Path("{id}")
    fun getMovieInfo(@PathParam("id") id: Int): Uni<MovieInfo?>? =
        Uni.createFrom()
            .publisher<MovieDetails?>(
                tmdbClient.movie
                    .details(
                        id,
                        LocaleCode.de_DE,
                        MovieRequest.Changes,
                        MovieRequest.Credits,
                        MovieRequest.Lists,
                        MovieRequest.ReleaseDates
                    )
            )
            .map { movieDetails: MovieDetails? ->
                MovieInfo(
                    movieDetails!!.imdbId,
                    movieDetails.title,
                    movieDetails.releaseDate!!.year
                )
            }

    data class MovieInfo(val imdbId: String?, val title: String?, val releaseYear: Int) {
        override fun toString(): String {
            return "MovieInfo[imdbId=$imdbId, title=$title, releaseYear=$releaseYear]"
        }
    }
}
