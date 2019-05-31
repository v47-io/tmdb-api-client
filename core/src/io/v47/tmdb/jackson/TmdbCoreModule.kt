package io.v47.tmdb.jackson

import com.fasterxml.jackson.databind.module.SimpleModule
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.jackson.mixins.RawErrorResponseMixin

class TmdbCoreModule : SimpleModule("TmdbCoreModule") {
    init {
        doInit()
    }

    private fun doInit() {
        setMixInAnnotation(RawErrorResponse::class.java, RawErrorResponseMixin::class.java)
    }
}
