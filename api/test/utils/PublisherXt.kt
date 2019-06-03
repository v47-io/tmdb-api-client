package io.v47.tmdb.utils

import io.reactivex.Flowable
import org.reactivestreams.Publisher

fun <T> Publisher<T>.blockingFirst(): T =
    Flowable.fromPublisher(this).blockingFirst()
