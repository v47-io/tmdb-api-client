@file:Suppress("MagicNumber")

package io.v47.tmdb.http.impl

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.utils.TypeInfo
import io.v47.tmdb.utils.checkPage
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo

internal data class TmdbRequest<T : Any>(
    val method: HttpMethod,
    val path: String,
    val apiVersion: Int,
    val queryArgs: Map<String, List<Any>>,
    val requestEntity: Any?,
    val responseType: TypeInfo
)

internal inline fun <T : Any> request(block: TmdbRequestBuilder<T>.() -> Unit): TmdbRequest<T> =
    TmdbRequestBuilderImpl<T>().apply(block).build()

internal inline fun <reified T : Any> request(path: String, block: TmdbRequestBuilder<T>.() -> Unit = {}) =
    request<T> {
        path(path)
        responseType(tmdbTypeReference<T>().toTypeInfo())

        block()
    }

internal inline fun <reified T : Any> get(path: String, block: TmdbRequestBuilder<T>.() -> Unit = {}) =
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

internal inline fun <reified T : Any> requestV4(path: String, block: TmdbRequestBuilder<T>.() -> Unit = {}) =
    request<T> {
        apiVersion(4)
        path(path)
        responseType(tmdbTypeReference<T>().toTypeInfo())

        block()
    }

internal inline fun <reified T : Any> getV4(path: String, block: TmdbRequestBuilder<T>.() -> Unit = {}) =
    requestV4<T>(path) {
        block()
        method(HttpMethod.Get)
    }

internal inline fun <reified T : Any> request(
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

internal inline fun <reified T : Any> requestV4(
    path: String,
    requestEntity: Any,
    block: TmdbRequestBuilder<T>.() -> Unit = {}
) =
    request<T> {
        method(HttpMethod.Post)
        apiVersion(4)
        path(path)
        requestEntity(requestEntity)
        responseType(tmdbTypeReference<T>().toTypeInfo())

        block()
    }

internal interface TmdbRequestBuilder<T : Any> {
    fun method(httpMethod: HttpMethod)
    fun apiVersion(apiVersion: Int)
    fun path(path: String)
    fun queryArg(name: String, value: Any, replace: Boolean = false)
    fun requestEntity(requestEntity: Any?)
    fun responseType(responseType: TypeInfo)
}

@Suppress("MagicNumber")
internal class TmdbRequestBuilderImpl<T : Any> : TmdbRequestBuilder<T> {
    private var _method = HttpMethod.Get
    private var _apiVersion = 3
    private var _path: String? = null
    private var _queryArgs = mutableMapOf<String, MutableList<Any>>()
    private var _requestEntity: Any? = null
    private var _responseType: TypeInfo? = null

    override fun method(httpMethod: HttpMethod) {
        _method = httpMethod
    }

    override fun apiVersion(apiVersion: Int) {
        _apiVersion = apiVersion
    }

    override fun path(path: String) {
        _path = path
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
        require(_apiVersion == 3 || _apiVersion == 4) { "This is not a valid request API version: $_apiVersion" }
        require(!_path.isNullOrBlank()) { "This is not a valid request path: $_path" }
        requireNotNull(_responseType) { "You must set a response type!" }

        return TmdbRequest(
            _method,
            _path!!,
            _apiVersion,
            _queryArgs,
            _requestEntity,
            _responseType!!
        )
    }
}
