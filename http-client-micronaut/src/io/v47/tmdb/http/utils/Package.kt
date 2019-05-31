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

private fun TypeInfo.Generic.toArgument(): Argument<*> =
    Argument.of(
        rawType,
        *typeArguments.map { it.toArgument() }.toTypedArray()
    )
