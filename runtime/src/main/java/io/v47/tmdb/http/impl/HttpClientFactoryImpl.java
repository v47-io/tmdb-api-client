package io.v47.tmdb.http.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.v47.tmdb.http.HttpClient;
import io.v47.tmdb.http.HttpClientFactory;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

public class HttpClientFactoryImpl implements HttpClientFactory {
    private final Vertx vertx;
    private final ObjectMapper objectMapper;

    public HttpClientFactoryImpl(@NotNull Vertx vertx, @NotNull ObjectMapper objectMapper) {
        this.vertx = vertx;
        this.objectMapper = objectMapper;
    }

    @NotNull
    @Override
    public HttpClient createHttpClient(@NotNull String baseUrl) {
        return new HttpClientImpl(baseUrl, WebClient.create(this.vertx), this.objectMapper);
    }
}
