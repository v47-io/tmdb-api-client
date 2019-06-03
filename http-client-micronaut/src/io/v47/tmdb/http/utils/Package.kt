package io.v47.tmdb.http.utils

import io.micronaut.core.type.Argument
import io.v47.tmdb.utils.TypeInfo

internal fun TypeInfo.toArgument() =
    when (this) {
        is TypeInfo.Simple -> toArgument()
        is TypeInfo.Generic -> toArgument()
    }

private fun TypeInfo.Simple.toArgument(): Argument<*> =
    Argument.of(type)

@Suppress("SpreadOperator")
private fun TypeInfo.Generic.toArgument(): Argument<*> =
    Argument.of(
        rawType,
        *typeArguments.map { it.toArgument() }.toTypedArray()
    )

@Suppress("MagicNumber")
internal fun getBasePath(baseUrl: String) =
    baseUrl.substring(8).let { url ->
        val part = url.substringAfter('/', "")
        if (part != "")
            "/$part"
        else
            ""
    }
