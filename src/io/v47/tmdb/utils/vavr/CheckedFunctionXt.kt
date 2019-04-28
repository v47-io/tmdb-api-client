package io.v47.tmdb.utils.vavr

import io.vavr.CheckedFunction0

operator fun <T> CheckedFunction0<T>.invoke(): T =
    apply()
