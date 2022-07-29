package io.v47.tmdb.http.quarkus;

import io.quarkus.arc.Priority;
import io.v47.tmdb.api.key.TmdbApiKeyProvider;
import io.v47.tmdb.http.impl.ClientConfig;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

@Alternative
@Priority(1)
@ApplicationScoped
public class StaticApiKeyProvider implements TmdbApiKeyProvider {
    private final ClientConfig config;

    @Inject
    public StaticApiKeyProvider(ClientConfig config) {
        this.config = config;
    }

    @NotNull
    @Override
    public String getApiKey() {
        return config.apiKey;
    }
}
