package io.v47.tmdb.cache

import io.github.resilience4j.cache.Cache
import io.v47.tmdb.utils.pack
import io.vavr.CheckedFunction0

internal class PackedCache(private val cache: Cache<ByteArray, ByteArray>) : Cache<ByteArray, ByteArray> {
    @Suppress("UNCHECKED_CAST")
    override fun computeIfAbsent(key: ByteArray, supplier: CheckedFunction0<ByteArray>) =
            computeIfAbsent(key as Any, supplier as CheckedFunction0<Any>)

    fun computeIfAbsent(key: Any, supplier: CheckedFunction0<Any>): ByteArray {
        val cacheKey = if (key is ByteArray)
            key
        else
            pack(key)

        return cache.computeIfAbsent(cacheKey) {
            val supplied = supplier.apply()
            if (supplied is ByteArray)
                supplied
            else
                pack(supplied)
        }
    }

    override fun getName() =
            "${cache.name}(packed)"

    override fun getMetrics(): Cache.Metrics =
            cache.metrics

    override fun getEventPublisher(): Cache.EventPublisher =
            cache.eventPublisher
}
