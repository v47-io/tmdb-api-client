package io.v47.tmdb.http.quarkus;

import io.smallrye.mutiny.Uni;
import io.v47.tmdb.TmdbClient;
import io.v47.tmdb.api.MovieRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MovieInfoResource {
    private final TmdbClient tmdbClient;

    @Inject
    public MovieInfoResource(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    @GET
    @Path("{id}")
    public Uni<MovieInfo> getMovieInfo(@PathParam("id") int id) {
        return Uni.createFrom()
                  .publisher(tmdbClient.getMovie()
                                       .details(id,
                                                null,
                                                MovieRequest.Changes,
                                                MovieRequest.Credits,
                                                MovieRequest.Lists))
                  .map(movieDetails -> new MovieInfo(movieDetails.getImdbId(),
                                                     movieDetails.getTitle(),
                                                     Objects.requireNonNull(movieDetails.getReleaseDate()).getYear()));
    }

    static class MovieInfo {
        public final String imdbId;
        public final String title;
        public final int releaseYear;

        public MovieInfo(String imdbId, String title, int releaseYear) {
            this.imdbId = imdbId;
            this.title = title;
            this.releaseYear = releaseYear;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            MovieInfo movieInfo = (MovieInfo) o;
            return releaseYear == movieInfo.releaseYear && imdbId.equals(movieInfo.imdbId) && title.equals(movieInfo.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(imdbId, title, releaseYear);
        }

        @Override
        public String toString() {
            return "MovieInfo{" + "imdbId='" + imdbId + '\'' + ", title='" + title + '\'' + ", releaseYear=" + releaseYear + '}';
        }
    }
}
