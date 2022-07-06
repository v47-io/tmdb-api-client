# Module api

Provides the actual TMDb API and the client, providing the class
[TmdbClient][io.v47.tmdb.TmdbClient] as the main entrypoint for all supported operations.

It requires an implementation of [HttpClientFactory][io.v47.tmdb.http.HttpClientFactory], so you
need to either use one of the provided `http-client-*` modules or create your own.

# Package io.v47.tmdb.api

Contains the implementations of the actual API calls.

# Package io.v47.tmdb.jackson

Provides Jackson specific functionality to assist with serialization and deserialization of TMDb API
client types.

Typically, you don't need to add the [TmdbApiModule][io.v47.tmdb.jackson.TmdbApiModule] manually,
because the module provides the required metadata for it to be registered automatically.

# Package io.v47.tmdb.model

Contains all types that form the data model used by the TMDb API client.
