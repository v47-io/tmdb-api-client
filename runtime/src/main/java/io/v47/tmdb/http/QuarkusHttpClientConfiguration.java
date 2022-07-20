package io.v47.tmdb.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.v47.tmdb.TmdbClient;
import io.v47.tmdb.http.impl.ClientBuildTimeConfig;
import io.v47.tmdb.http.impl.HttpClientFactoryImpl;
import io.vertx.mutiny.core.Vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class QuarkusHttpClientConfiguration {
    private final ClientBuildTimeConfig config;
    private final Vertx vertx;
    private final ObjectMapper objectMapper;

    @Inject
    public QuarkusHttpClientConfiguration(ClientBuildTimeConfig config, Vertx vertx, ObjectMapper objectMapper) {
        this.config = config;
        this.vertx = vertx;
        this.objectMapper = objectMapper;
    }

    @Produces
    @Default
    @ApplicationScoped
    public HttpClientFactory httpClientFactory() {
        return new HttpClientFactoryImpl(this.vertx, this.objectMapper);
    }

    @Produces
    @Default
    @ApplicationScoped
    public TmdbClient tmdbClient(HttpClientFactory httpClientFactory) {
        return new TmdbClient(httpClientFactory, this.config.apiKey);
    }
}
