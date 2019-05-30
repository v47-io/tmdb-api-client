package io.v47.tmdb.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

sealed class TypeInfo {
    data class Simple(val type: Class<*>) : TypeInfo()
    data class Generic(val rawType: Class<*>, val typeArguments: List<TypeInfo>) : TypeInfo()
}

internal inline fun <reified T : Any> tmdbTypeReference() = object : TmdbTypeReference<T>() {}

internal abstract class TmdbTypeReference<T : Any> protected constructor() : Comparable<TmdbTypeReference<T>> {
    val type: Type

    init {
        val superClass = javaClass.genericSuperclass
        if (superClass is Class<*>)
            throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")

        type = (superClass as ParameterizedType).actualTypeArguments[0]
    }

    override fun compareTo(other: TmdbTypeReference<T>) = 0
}

internal fun TmdbTypeReference<*>.toTypeInfo() = type.toTypeInfo()

private fun Type.toTypeInfo(): TypeInfo =
    when (this) {
        is Class<*> -> toTypeInfo()
        is ParameterizedType -> toTypeInfo()
        is WildcardType -> when {
            upperBounds.size == 1 -> upperBounds[0].toTypeInfo()
            lowerBounds.size == 1 -> lowerBounds[0].toTypeInfo()
            else -> throw IllegalArgumentException("Cannot convert wildcard type without singular bound!")
        }
        else -> throw IllegalArgumentException("Cannot convert ${javaClass.canonicalName} into TypeInfo!")
    }

private fun ParameterizedType.toTypeInfo(): TypeInfo {
    val typeArgumentInfos = actualTypeArguments.map { it.toTypeInfo() }

    return if (typeArgumentInfos.isNotEmpty())
        TypeInfo.Generic(rawType as Class<*>, typeArgumentInfos)
    else
        TypeInfo.Simple(rawType as Class<*>)
}

private fun Class<*>.toTypeInfo(): TypeInfo = TypeInfo.Simple(this)
