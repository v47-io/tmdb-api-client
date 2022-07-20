package io.v47.tmdb.http.quarkus;

import io.v47.tmdb.http.HttpClientFactory;
import io.v47.tmdb.http.tck.HttpClientTck;
import io.v47.tmdb.http.tck.TckResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/run-tck-test")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TckResource {
    private final HttpClientFactory httpClientFactory;

    @Inject
    public TckResource(HttpClientFactory httpClientFactory) {
        this.httpClientFactory = httpClientFactory;
    }

    @GET
    public TckResult runTckTest() {
        HttpClientTck tck = new HttpClientTck();

        return tck.verify(this.httpClientFactory);
    }
}
