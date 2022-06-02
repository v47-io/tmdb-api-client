/**
 * Copyright 2022 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.v47.tmdb.utils

import io.v47.tmdb.http.TypeInfo
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

inline fun <reified T : Any> tmdbTypeReference() = object : TmdbTypeReference<T>() {}

abstract class TmdbTypeReference<T : Any> protected constructor() :
    Comparable<TmdbTypeReference<T>> {

    val type: Type

    init {
        val superClass = javaClass.genericSuperclass
        if (superClass is Class<*>)
            throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")

        type = (superClass as ParameterizedType).actualTypeArguments[0]
    }

    override fun compareTo(other: TmdbTypeReference<T>) = 0
}

fun TmdbTypeReference<*>.toTypeInfo() = type.toTypeInfo()

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
        TypeInfo.Generic(
            rawType as Class<*>,
            typeArgumentInfos,
            this
        )
    else
        TypeInfo.Simple(rawType as Class<*>)
}

private fun Class<*>.toTypeInfo(): TypeInfo = TypeInfo.Simple(this)
