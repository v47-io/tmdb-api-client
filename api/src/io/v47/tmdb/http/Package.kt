/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.http

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.impl.ApiVersion
import io.v47.tmdb.http.impl.TmdbRequest
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
