# Module http-client-micronaut

Provides an implementation of [HttpClient][io.v47.tmdb.http.HttpClient] that uses the Micronaut
HTTP client.

This module contains two implementations of [HttpClientFactory][io.v47.tmdb.http.HttpClientFactory],
one to use with a `BeanContext` and one without.

Additionally, this module also provides reflection configuration for the TMDb API client model, so
it can be used in Micronaut applications compiled as native executables using GraalVM.
