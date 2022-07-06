# Module core

Contains the core types for the TMDb API client.

# Package io.v47.tmdb.http

Contains the interfaces that need to implemented when providing an HTTP client for the TMDb API
client.

The [HttpClient][io.v47.tmdb.http.HttpClient] is reactive, so it can properly integrate with any
concurrency model.

# Package io.v47.tmdb.jackson

Provides Jackson specific functionality to assist with serialization and deserialization of TMDb API
client types.

Typically, you don't need to add the [TmdbCoreModule][io.v47.tmdb.jackson.TmdbCoreModule] manually,
because the module provides the required metadata for it to be registered automatically.
