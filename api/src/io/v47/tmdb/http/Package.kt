/**
 * Copyright 2022 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.v47.tmdb.http

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.impl.ApiVersion
import io.v47.tmdb.http.impl.TmdbRequest
import io.v47.tmdb.utils.TypeInfo
import io.v47.tmdb.utils.checkPage
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo

internal inline fun <T : Any> request(block: TmdbRequestBuilder<T>.() -> Unit): TmdbRequest<T> =
    TmdbRequestBuilderImpl<T>().apply(block).build()

internal inline fun <reified T : Any> request(
    path: String,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    request<T> {
        path(path)
        responseType(tmdbTypeReference<T>().toTypeInfo())

        block()
    }

internal inline fun <reified T : Any> get(
    path: String,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    request<T>(path) {
        block()
        method(HttpMethod.Get)
    }

internal inline fun <reified T : Any> getWithLanguage(
    path: String,
    language: LocaleCode? = null,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    get<T>(path) {
        block()

        language?.let { queryArg("language", it.toString(), replace = true) }
    }

internal inline fun <reified T : Any> getWithPage(
    path: String,
    page: Int? = null,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    get<T>(path) {
        block()

        page?.let {
            checkPage(it)
            queryArg("page", it.toString(), replace = true)
        }
    }

internal inline fun <reified T : Any> getWithPageAndLanguage(
    path: String,
    page: Int? = null,
    language: LocaleCode? = null,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    get<T>(path) {
        block()

        page?.let {
            checkPage(it)
            queryArg("page", it.toString(), replace = true)
        }

        language?.let { queryArg("language", it.toString(), replace = true) }
    }

@Suppress("MagicNumber")
internal inline fun <reified T : Any> requestV4(
    path: String,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    request<T> {
        apiVersion(ApiVersion.V4)
        path(path)
        responseType(tmdbTypeReference<T>().toTypeInfo())

        block()
    }

internal inline fun <reified T : Any> getV4(
    path: String,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    requestV4<T>(path) {
        block()
        method(HttpMethod.Get)
    }

internal inline fun <reified T : Any> post(
    path: String,
    requestEntity: Any,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    request<T> {
        method(HttpMethod.Post)
        path(path)
        requestEntity(requestEntity)
        responseType(tmdbTypeReference<T>().toTypeInfo())

        block()
    }

@Suppress("MagicNumber")
internal inline fun <reified T : Any> postV4(
    path: String,
    requestEntity: Any,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    requestV4<T>(path) {
        block()

        method(HttpMethod.Post)
        requestEntity(requestEntity)
    }

internal interface TmdbRequestBuilder<T : Any> {
    fun method(httpMethod: HttpMethod)
    fun apiVersion(apiVersion: ApiVersion)
    fun path(path: String)
    fun pathVar(name: String, value: Any)
    fun queryArg(name: String, value: Any, replace: Boolean = false)
    fun requestEntity(requestEntity: Any?)
    fun responseType(responseType: TypeInfo)
}

@Suppress("MagicNumber")
internal class TmdbRequestBuilderImpl<T : Any> : TmdbRequestBuilder<T> {
    private var _method = HttpMethod.Get
    private var _apiVersion = ApiVersion.V3
    private var _path: String? = null
    private var _pathVariables = mutableMapOf<String, Any>()
    private var _queryArgs = mutableMapOf<String, MutableList<Any>>()
    private var _requestEntity: Any? = null
    private var _responseType: TypeInfo? = null

    override fun method(httpMethod: HttpMethod) {
        _method = httpMethod
    }

    override fun apiVersion(apiVersion: ApiVersion) {
        _apiVersion = apiVersion
    }

    override fun path(path: String) {
        _path = path
    }

    override fun pathVar(name: String, value: Any) {
        _pathVariables.putIfAbsent(name, value)
    }

    override fun queryArg(name: String, value: Any, replace: Boolean) {
        if (!replace)
            _queryArgs.computeIfAbsent(name) { mutableListOf() }.add(value)
        else
            _queryArgs[name] = mutableListOf(value)
    }

    override fun requestEntity(requestEntity: Any?) {
        _requestEntity = requestEntity
    }

    override fun responseType(responseType: TypeInfo) {
        _responseType = responseType
    }

    fun build(): TmdbRequest<T> {
        require(!_path.isNullOrBlank()) { "This is not a valid request path: $_path" }
        requireNotNull(_responseType) { "You must set a response type!" }

        return TmdbRequest(
            _method,
            _path!!,
            _pathVariables,
            _apiVersion,
            _queryArgs,
            _requestEntity,
            _responseType!!
        )
    }
}
