package io.v47.tmdb.jackson.mixins

import com.fasterxml.jackson.annotation.JsonAnySetter

internal interface TmdbTypeMixin {
    @JsonAnySetter
    fun handleUnknownProperty(key: String, value: Any?)
}
