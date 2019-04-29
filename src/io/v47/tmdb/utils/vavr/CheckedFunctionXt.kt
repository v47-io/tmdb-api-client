package io.v47.tmdb.utils.vavr

import io.vavr.CheckedFunction0

internal operator fun <T> CheckedFunction0<T>.invoke(): T =
    apply()
