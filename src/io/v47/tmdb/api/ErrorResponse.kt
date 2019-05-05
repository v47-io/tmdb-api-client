package io.v47.tmdb.api

import io.v47.tmdb.TmdbClientException
import io.v47.tmdb.http.HttpRequest

data class RawErrorResponse(
    val statusMessage: String,
    val statusCode: Int,
    val success: Boolean?,
    val errorMessage: String?
)

fun RawErrorResponse.toErrorResponse() =
    ErrorResponse(errorMessage ?: statusMessage, statusCode)

data class ErrorResponse internal constructor(
    val message: String,
    val code: Int
)

class ErrorResponseException(val response: ErrorResponse, request: HttpRequest) :
    TmdbClientException("[${response.code}] ${response.message}", request)
