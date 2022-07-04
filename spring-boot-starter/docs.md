# Module spring-boot-starter

Autoconfigures a [TmdbClient][io.v47.tmdb.TmdbClient] bean for projects using Spring Webflux.

This requires either the property `tmdb-client.api-key` or the environment variable `TMDB_API_KEY`
to be set.

Furthermore, autoconfiguration is only done if there isn't already a `TmdbClient` bean, and you can
also override the default [HttpClientFactory][io.v47.tmdb.http.HttpClientFactory] bean by providing
your own.
