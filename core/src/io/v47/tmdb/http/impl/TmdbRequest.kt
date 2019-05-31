@file:Suppress("MagicNumber")

package io.v47.tmdb.http.impl

import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.utils.TypeInfo

data class TmdbRequest<T : Any>(
    val method: HttpMethod,
    val path: String,
    val apiVersion: Int,
    val queryArgs: Map<String, List<Any>>,
    val requestEntity: Any?,
    val responseType: TypeInfo
)
