package io.v47.tmdb.http.impl;

import io.v47.tmdb.api.key.TmdbApiKeyProvider;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

@Default
@ApplicationScoped
public class ConfigApiKeyProvider implements TmdbApiKeyProvider {
    private final String apiKey;

    public ConfigApiKeyProvider(ClientConfig config) {
        apiKey = config.apiKey;
    }

    @NotNull
    @Override
    public String getApiKey() {
        return apiKey;
    }
}
