package io.v47.tmdb.utils

import io.smallrye.mutiny.Multi
import java.util.concurrent.Flow

internal fun <T> Flow.Publisher<T>.blockingFirst(): T =
    Multi
        .createFrom()
        .publisher(this)
        .subscribe()
        .asIterable()
        .first()
